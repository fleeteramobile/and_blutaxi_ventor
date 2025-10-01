package com.blueventor.menu.driver

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.blueventor.R
import com.blueventor.databinding.FragmentDriveDashBoardBinding
import com.blueventor.databinding.FragmentDriverInfoBinding
import com.blueventor.network.UiState
import com.blueventor.network.request.RequestDriverDetails
import com.blueventor.network.response.ResponseDriverDetails
import com.blueventor.network.viewmodel.DriverDetailsViewModel
import com.blueventor.session.SessionManager
import com.blueventor.util.logDebugMessage
import com.blueventor.util.onclik
import com.blueventor.util.setOnclick
import com.blueventor.util.showDatePicker
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class DriveDashBoardFragment : Fragment() {

    @Inject
    lateinit var sessionManager: SessionManager
    private var _binding: FragmentDriveDashBoardBinding? = null
    private val binding get() = _binding!!
    val driverDetailsViewModel: DriverDetailsViewModel by viewModels()
    var company_id = ""
    var driver_id = ""
    var currentDate = ""
    var firstDay = ""
    var driver_licence = ""
    var driver_licence_back_side = ""
    var taxi_image = ""
    var vehicle_registration_license = ""
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

        _binding = FragmentDriveDashBoardBinding.inflate(inflater, container, false)
        return binding.root        // Inflate the layout for this fragment
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.documents.onclik {

            findNavController().navigate(R.id.driverPersonalInfoFragment)
        }

        binding.walletRecharge.onclik {

            findNavController().navigate(R.id.driverRechargeFragment)
        }
        binding.driverDetails.onclik {
            findNavController().navigate(R.id.driverInfoFragment)
        }
        binding.ivBack.onclik {
            requireActivity().onBackPressed()
        }
        _binding!!.startDateTxt.setText(firstDay)
        _binding!!.endDateTxt.setText(currentDate)

        _binding!!.startDate.setOnClickListener {
            showDatePicker(_binding!!.startDateTxt)
        }

        _binding!!.endDateTxt.setOnClickListener {
            showDatePicker(_binding!!.startDateTxt)
        }

        setOnclick(_binding!!.btnGo)
        {
            loadDriverDetaislAPI()
            getDrierDetailsDetails()
        }
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

                            _binding!!.driverName.setText(response.data.get(0).name)
                            _binding!!.carPlate.setText(response.data.get(0).taxi_no)
                            _binding!!.tvTotalRides.setText(response.data.get(0).total_trips_count)
                            _binding!!.tvCompletedRides.setText(response.data.get(0).completed_trips_count)
                            _binding!!.tvCancelledRides.setText(response.data.get(0).cancel_trips_count)
                            _binding!!.tvtotalearnings.setText(response.data.get(0).today_earnings)

                            _binding!!.tvWalletBalance.setText(response.data.get(0).account_balance)

                            _binding!!.tvloginHours.setText(response.data.get(0).today_login_hours)

                            driver_licence = response.data.get(0).driver_licence
                            driver_licence_back_side = response.data.get(0).driver_licence_back_side
                            taxi_image = response.data.get(0).taxi_image
                            vehicle_registration_license =
                                response.data.get(0).vehicle_registration_license
                            sessionManager.saveString("driver_licence", driver_licence)
                            sessionManager.saveString(
                                "driver_licence_back_side",
                                driver_licence_back_side
                            )
                            sessionManager.saveString("taxi_image", taxi_image)
                            sessionManager.saveString(
                                "vehicle_registration_license",
                                vehicle_registration_license
                            )
                        }
                    }
                }
            }
        }
    }

    private fun loadDriverDetaislAPI() {
        lifecycleScope.launch {
            val request = RequestDriverDetails(
                company_id = company_id, start_date = _binding!!.startDateTxt.text.toString(),
                end_date = _binding!!.endDateTxt.text.toString(),
                driver_id = driver_id

            )
            driverDetailsViewModel.getDriverDetails(request)
        }
    }

    override fun onResume() {
        super.onResume()
        loadDriverDetaislAPI()
        getDrierDetailsDetails()
    }
}