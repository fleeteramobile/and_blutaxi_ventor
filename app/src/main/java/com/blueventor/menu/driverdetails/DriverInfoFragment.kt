package com.blueventor.menu.driverdetails

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.blueventor.R
import com.blueventor.databinding.FragmentDriverDetailsBinding
import com.blueventor.databinding.FragmentDriverInfoBinding
import com.blueventor.menu.driverlist.DriverListAdapter
import com.blueventor.network.UiState
import com.blueventor.network.request.RequestDashBoardCarPerformanceDetails
import com.blueventor.network.request.RequestDriverDetails
import com.blueventor.network.response.ResponseCarPerformance
import com.blueventor.network.response.ResponseDriverDetails
import com.blueventor.session.SessionManager
import com.blueventor.util.logDebugMessage
import com.blueventor.util.onclik
import com.blueventor.viewmodel.DashboardViewModel
import com.blueventor.viewmodel.DriverDetailsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class DriverInfoFragment : Fragment() {
    @Inject
    lateinit var sessionManager: SessionManager
    private var _binding: FragmentDriverInfoBinding? = null
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
                            _binding!!.etFirstName.setText(response.data.get(0).name)
                            _binding!!.etLastName.setText(response.data.get(0).lastname)
                            _binding!!.tvWalletBalance.setText(response.data.get(0).account_balance)

                            _binding!!.etEmail.setText(response.data.get(0).email)
                            _binding!!.etPhone.setText(response.data.get(0).phone)
                            _binding!!.tvSelectedDob.setText(response.data.get(0).name)
                            _binding!!.etDriverLicenseId.setText(response.data.get(0).driver_license_id)
                            _binding!!.etAddress.setText(response.data.get(0).address)
                            _binding!!.etCityName.setText(response.data.get(0).city_name)
                            _binding!!.etStateName.setText(response.data.get(0).state_name)
                            _binding!!.etCountryName.setText(response.data.get(0).country_name)
                            _binding!!.etTaxiFCExpiryDate.setText(response.data.get(0).taxi_fc_expiry_date)
                            _binding!!.etTaxiPermitExpiryDate.setText(response.data.get(0).taxi_permit_expiry_date)
                            _binding!!.etMappingStartDate.setText(response.data.get(0).mapping_startdate)
                            _binding!!.etMappingEndDate.setText(response.data.get(0).mapping_enddate)
                            driver_licence = response.data.get(0).driver_licence
                            driver_licence_back_side = response.data.get(0).driver_licence_back_side
                            taxi_image = response.data.get(0).taxi_image
                            vehicle_registration_license =
                                response.data.get(0).vehicle_registration_license
                            sessionManager.saveString("driver_licence",driver_licence)
                            sessionManager.saveString("driver_licence_back_side",driver_licence_back_side)
                            sessionManager.saveString("taxi_image",taxi_image)
                            sessionManager.saveString("vehicle_registration_license",vehicle_registration_license)
                        }
                    }
                }
            }
        }
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentDriverInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        loadDriverDetaislAPI()
        getDrierDetailsDetails()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.ivBack.setOnClickListener {
            requireActivity().onBackPressed()
        }

        binding.documents.onclik {
//driverRechargeFragment
            //driverPersonalInfoFragment
            findNavController().navigate(R.id.driverRechargeFragment)
        }
    }
}