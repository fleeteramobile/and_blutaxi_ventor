package com.blueventor.menu.driver

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.blueventor.databinding.FragmentAllDriversListBinding
import com.blueventor.databinding.FragmentDriverPersonalInfoBinding
import com.blueventor.databinding.FragmentDriverRechargeBinding
import com.blueventor.menu.driver.driverlist.DriverListAdapter
import com.blueventor.network.UiState
import com.blueventor.network.request.RequestDriverDetails
import com.blueventor.network.request.RequestDriverRecharge
import com.blueventor.network.response.ResponseCarPerformance
import com.blueventor.network.response.ResponseDriverDetails
import com.blueventor.network.response.ResponseDriverWalletRecharge
import com.blueventor.network.viewmodel.DashboardViewModel
import com.blueventor.network.viewmodel.DriverDetailsViewModel
import com.blueventor.network.viewmodel.DriverRechargeViewModel
import com.blueventor.session.SessionManager
import com.blueventor.util.logDebugMessage
import com.blueventor.util.onclik
import com.blueventor.util.setOnclick
import com.blueventor.util.showAlert
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class DriverRechargeFragment : Fragment(), PaymentResultListener {
    lateinit var _binding: FragmentDriverRechargeBinding

    @Inject
    lateinit var sessionManager: SessionManager
    private val binding get() = _binding!!
    var driver_id = ""
    var company_id = ""
    var currentDate = ""
    var firstDay = ""
    val driverDetailsViewModel: DriverDetailsViewModel by viewModels()
    var accountBalance = 0.0
    var rechargeLimit = 0.0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        company_id = sessionManager.getString("company_id", "Not Found")
        driver_id = sessionManager.getString("driver_id", "Not Found")
        val calendar = Calendar.getInstance()


        currentDate = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(calendar.time)
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        firstDay = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(calendar.time)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentDriverRechargeBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.backPress.onclik {
            requireActivity().onBackPressed()
        }

        setOnclick(_binding.addmoneyBut)
        {
            val enteredAmount = _binding.addmoneyEdt.text.toString().toDoubleOrNull() ?: 0.0


            val totalAmount = accountBalance + enteredAmount

            if (totalAmount > rechargeLimit) {
                requireActivity().showAlert("Recharge not allowed! Max limit is $rechargeLimit (Available balance: $accountBalance)")
                // Not allowed

            } else {
                // Allowed → Start payment
                startPayment()
            }
        }
        loadDriverDetaislAPI()
        getDrierDetailsDetails()
    }

    private fun loadDriverDetaislAPI() {
        lifecycleScope.launch {
            val request = RequestDriverDetails(
                company_id = company_id, start_date = firstDay,
                end_date = currentDate,
                driver_id = driver_id

            )
            driverDetailsViewModel.getDriverDetails(request)
        }
    }

    private fun startPayment() {

        sessionManager.saveString("recharge_amount", _binding.addmoneyEdt.text.toString())
        val checkout = Checkout()
        checkout.setKeyID("rzp_test_E9tlWpCq61sn6x") // 🔑 Replace with your Razorpay Key
        val enteredAmount = _binding.addmoneyEdt.text.toString().toDoubleOrNull() ?: 0.0

        try {
            val options = JSONObject()
            options.put("name", "Blue Taxi")
            options.put("description", "Booking Payment")
            options.put("currency", "INR")
            options.put("amount", (enteredAmount * 100).toInt()) // amount in paise (₹500)

            // Razorpay requires an Activity reference
            checkout.open(requireActivity(), options)

        } catch (e: Exception) {
            e.printStackTrace()
            println("Payment_error" + "" + e.message)
            Toast.makeText(requireContext(), "Error in payment: ${e.message}", Toast.LENGTH_LONG)
                .show()
        }
    }

    override fun onPaymentSuccess(p0: String?) {

    }

    override fun onPaymentError(p0: Int, p1: String?) {

    }

    private fun getDrierDetailsDetails() {
        lifecycleScope.launch {
            driverDetailsViewModel.uiState.collect { cardetails ->
                when (cardetails) {
                    is UiState.Error -> {
                        logDebugMessage("getdriverdetails", cardetails.message)
                    }

                    UiState.Idle -> {

                    }

                    UiState.Loading -> {}
                    is UiState.Success -> {
                        val response = cardetails.data as ResponseDriverDetails
                        if (response != null) {

                            _binding!!.walletbalTxt.setText("₹ ${response.data.get(0).account_balance}")
                            response.data.get(0).account_balance
                            _binding.driverLimit.setText(
                                "Driver maximum recharge limit ${
                                    response.data.get(
                                        0
                                    ).recharge_limit
                                } "
                            )
                            accountBalance = response.data.get(0).account_balance.toDouble()
                            rechargeLimit = response.data.get(0).recharge_limit.toDouble()
                        }
                    }
                }
            }
        }
    }
}
