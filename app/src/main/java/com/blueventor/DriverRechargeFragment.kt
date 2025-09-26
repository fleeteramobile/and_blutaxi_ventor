package com.blueventor

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.blueventor.databinding.FragmentAllDriversListBinding
import com.blueventor.databinding.FragmentDriverPersonalInfoBinding
import com.blueventor.databinding.FragmentDriverRechargeBinding
import com.blueventor.session.SessionManager
import com.blueventor.util.setOnclick
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject
import javax.inject.Inject

@AndroidEntryPoint
class DriverRechargeFragment : Fragment(), PaymentResultListener {
lateinit var _binding: FragmentDriverRechargeBinding
    @Inject
    lateinit var sessionManager: SessionManager
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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

        setOnclick(_binding.addmoneyBut)
        {
            startPayment()
        }
    }
    private fun startPayment() {
        val checkout = Checkout()
        checkout.setKeyID("rzp_test_E9tlWpCq61sn6x") // 🔑 Replace with your Razorpay Key

        try {
            val options = JSONObject()
            options.put("name", "Blue Taxi")
            options.put("description", "Booking Payment")
            options.put("currency", "INR")
            options.put("amount", "500") // amount in paise (₹500)

            // Razorpay requires an Activity reference
            checkout.open(requireActivity(), options)

        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(requireContext(), "Error in payment: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    override fun onPaymentSuccess(p0: String?) {

    }

    override fun onPaymentError(p0: Int, p1: String?) {

    }


}