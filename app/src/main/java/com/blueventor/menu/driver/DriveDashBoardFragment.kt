package com.blueventor.menu.driver

import android.content.Context
import android.os.Bundle
import android.text.format.DateUtils.formatDateTime
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
import com.blueventor.util.setImageOnclick
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

        binding.walletRecharge.onclik {

            findNavController().navigate(R.id.driverRechargeFragment)
        }

        binding.itemDocuments.onclik {

            findNavController().navigate(R.id.driverPersonalInfoFragment)
        }


        binding.itemDriverDetails.onclik {
            findNavController().navigate(R.id.driverInfoFragment)
        }


        binding.itemTripHistory.onclik {
            findNavController().navigate(R.id.SIngleDriverTripListFragment)
        }

        binding.itemRechargeHistory.onclik {
            findNavController().navigate(R.id.driverRechargeLogsFragment)
        }
        binding.itemAttendanceLogs.setOnClickListener {

            findNavController().navigate(R.id.driverAttendanceFragment)

        }

        binding.backIcon.onclik {
            requireActivity().onBackPressed()
        }

        binding.dateFlitter.setOnClickListener {

        }



        _binding!!.startDate.setOnClickListener {
            showDatePicker(_binding!!.startDate)
        }

        _binding!!.endDate.setOnClickListener {
            showDatePicker(_binding!!.endDate)
        }

        setImageOnclick(_binding!!.dateFlitter)
        {
            loadDriverDetaislAPIDate()
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

                            _binding!!.tvUserName.setText(response.data.get(0).name)
                            _binding!!.tvEmail.setText(response.data.get(0).email)
                            _binding!!.tvPhone.setText(
                                "${response.data.get(0).country_code} ${
                                    response.data.get(
                                        0
                                    ).phone
                                }"
                            )
                            // _binding!!.carPlate.setText(response.data.get(0).taxi_no)
                            _binding!!.tvTotalRides.setText(response.data.get(0).total_trips_count)
                            _binding!!.tvCompletedRides.setText(response.data.get(0).completed_trips_count)
                            _binding!!.tvCancelledRides.setText(response.data.get(0).cancel_trips_count)
                            _binding!!.tvTotalEarnings.setText(response.data.get(0).today_earnings)
                            _binding!!.tvSelectedCourse.setText(response.data.get(0).last_location_name)


                            _binding!!.tvWalletBalance.setText(response.data.get(0).account_balance)

                            _binding!!.totalLoginHours.setText(response.data.get(0).today_login_hours)

                            // Login status text
                            val loginStatusText = if (response.data[0].login_status == "S") {
                                "Login"
                            } else {
                                "Logout"
                            }


// Shift status text
                            val shiftStatusText = if (response.data[0].shift_status == "IN") {
                                "Shift In"
                            } else {
                                "Shift Out"
                            }
                            _binding!!.tvCurrentStatus.setText("$loginStatusText | $shiftStatusText")

                            val lastUpdated = response.data[0].last_location_updated
                            val formatted = formatTimeData(lastUpdated)

                            _binding!!.tvLastUpdate.text = formatted

                            driver_licence = response.data.get(0).driver_licence
                            driver_licence_back_side = response.data.get(0).driver_licence_back_side
                            taxi_image = response.data.get(0).taxi_image
                            sessionManager.saveString(
                                "driver_name",
                                response.data.get(0).name
                            )
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

    fun formatTimeData(input: String): String {
        return try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            val outputFormat = SimpleDateFormat("dd-MM-yyyy hh:mm a", Locale.getDefault())

            val date = inputFormat.parse(input)
            outputFormat.format(date!!)
        } catch (e: Exception) {
            input   // return original if error
        }
    }

    private fun loadDriverDetaislAPI() {
        lifecycleScope.launch {
            val request = RequestDriverDetails(
                company_id = company_id,
                start_date = currentDate,
                end_date = currentDate,
                driver_id = driver_id

            )
            driverDetailsViewModel.getDriverDetails(request)
        }
    }

    private fun loadDriverDetaislAPIDate() {
        lifecycleScope.launch {
            val request = RequestDriverDetails(
                company_id = company_id,
                start_date = _binding!!.startDate.text.toString(),
                end_date = _binding!!.endDate.text.toString(),
                driver_id = driver_id

            )
            driverDetailsViewModel.getDriverDetails(request)
        }
    }

    override fun onResume() {
        super.onResume()
        val calendar = Calendar.getInstance()


        currentDate = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(calendar.time)
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        firstDay = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(calendar.time)
        _binding!!.startDate.setText(currentDate)
        _binding!!.endDate.setText(currentDate)
        loadDriverDetaislAPI()
        getDrierDetailsDetails()
    }
}