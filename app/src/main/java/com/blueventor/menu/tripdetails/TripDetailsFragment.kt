package com.blueventor.menu.tripdetails

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
import com.blueventor.databinding.FragmentTripDetailsBinding
import com.blueventor.menu.triplist.TripListAdapter
import com.blueventor.network.UiState
import com.blueventor.network.request.RequestTripDetails
import com.blueventor.network.request.RequestTripList
import com.blueventor.network.response.ResponseTripDetails
import com.blueventor.network.response.ResponseTripList
import com.blueventor.session.SessionManager
import com.blueventor.util.logDebugMessage
import com.blueventor.network.viewmodel.DashboardViewModel
import com.blueventor.network.viewmodel.TripDetailsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class TripDetailsFragment : Fragment() {

    @Inject
    lateinit var sessionManager: SessionManager
    private var _binding: FragmentTripDetailsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    val tripDetailsViewModel: TripDetailsViewModel by viewModels()
    var company_id = ""
    var trip_id = ""
    var currentDate = ""
    var firstDay = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentTripDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        company_id = sessionManager.getString("company_id", "Not Found")
        trip_id = sessionManager.getString("trip_id", "Not Found")
        binding.backButton.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    override fun onResume() {
        super.onResume()
        loadTripDetailsAPI()
        getTripDetails()
    }

    private fun getTripDetails() {
        lifecycleScope.launch {
            tripDetailsViewModel.uiState.collect { cardetails ->
                when (cardetails) {
                    is UiState.Error -> {

                    }

                    UiState.Idle -> {

                    }

                    UiState.Loading -> {}
                    is UiState.Success -> {
                        val response = cardetails.data as ResponseTripDetails
                        if (response != null) {

                            if (response.status == 1) {


                                binding!!.tripId!!.setText("#${response.trip_details._id}")




                                binding!!.pickupLocation!!.text = response.trip_details.pickup_location
                                binding!!.dropLocation!!.text = response.trip_details.drop_location
                                binding!!.distance!!.text = response.trip_details.distance.toString() + " " + "KM"
                                binding!!.name.text = response.trip_details.passenger_name


                                binding!!.totalBill!!.text = "₹" + " " + response.trip_details.amt
                                binding!!.totalAmount!!.text = "₹" + " " + response.trip_details.amt






                            }

                        } else {

                        }
                    }
                }
            }
        }
    }

    private fun loadTripDetailsAPI() {
        lifecycleScope.launch {
            val request = RequestTripDetails(
                company_id = company_id,
                _id = trip_id

            )
            tripDetailsViewModel.getDriverDetails(request)
        }
    }
}