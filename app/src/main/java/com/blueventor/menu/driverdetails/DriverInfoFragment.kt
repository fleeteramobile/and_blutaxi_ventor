package com.blueventor.menu.driverdetails

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        company_id = sessionManager.getString("company_id", "Not Found")
        driver_id = sessionManager.getString("driver_id", "Not Found")
        val calendar = Calendar.getInstance()


        currentDate = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(calendar.time)
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        firstDay = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(calendar.time)

    }

    private fun getDrierDetailsDetails() {
        lifecycleScope.launch {
            driverDetailsViewModel.uiState.collect { cardetails ->
                when (cardetails) {
                    is UiState.Error -> {}
                    UiState.Idle -> {

                    }

                    UiState.Loading -> {}
                    is UiState.Success -> {
                        val response = cardetails.data as ResponseDriverDetails
                        if (response != null) {

                            _binding!!.driverName.setText(response.data.name)
                            _binding!!.carPlate.setText(response.data.taxi_no)
                            _binding!!.tvTotalRides.setText(response.data.total_trips_count)
                            _binding!!.tvCompletedRides.setText(response.data.completed_trips_count)
                            _binding!!.tvCancelledRides.setText(response.data.cancel_trips_count)
                            _binding!!.etFirstName.setText(response.data.name)
                            _binding!!.etLastName.setText(response.data.lastname)

                            _binding!!.etEmail.setText(response.data.email)
                            _binding!!.etPhone.setText(response.data.phone)
                            _binding!!.tvSelectedDob.setText(response.data.name)
                            _binding!!.etDriverLicenseId.setText(response.data.driver_license_id)
                            _binding!!.etAddress.setText(response.data.address)
                            _binding!!.etCityName.setText(response.data.city_name)
                            _binding!!.etStateName.setText(response.data.state_name)
                            _binding!!.etCountryName.setText(response.data.country_name)
                            _binding!!.etTaxiFCExpiryDate.setText(response.data.taxi_fc_expiry_date)
                            _binding!!.etTaxiPermitExpiryDate.setText(response.data.taxi_permit_expiry_date)
                            _binding!!.etMappingStartDate.setText(response.data.mapping_startdate)
                            _binding!!.etMappingEndDate.setText(response.data.mapping_enddate)

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
    }
}