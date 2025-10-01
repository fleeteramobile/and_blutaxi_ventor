package com.blueventor.menu.triplist

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
import com.blueventor.databinding.FragmentCabsListBinding
import com.blueventor.databinding.FragmentTripListBinding
import com.blueventor.menu.MainActivity
import com.blueventor.menu.cablist.CabListAdapter
import com.blueventor.network.UiState
import com.blueventor.network.request.RequestDriverDetails
import com.blueventor.network.request.RequestTripList
import com.blueventor.network.response.ResponseCarPerformance
import com.blueventor.network.response.ResponseTripList
import com.blueventor.session.SessionManager
import com.blueventor.util.showDatePicker
import com.blueventor.network.viewmodel.DashboardViewModel
import com.blueventor.network.viewmodel.TripListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class TripListFragment : Fragment(), ShowTripDetails {
    @Inject
    lateinit var sessionManager: SessionManager
    private var _binding: FragmentTripListBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    val tripListViewModel: TripListViewModel by viewModels()
    var company_id = ""
    var currentDate = ""
    var firstDay = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        company_id = sessionManager.getString("company_id", "Not Found")
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
        _binding = FragmentTripListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        company_id = sessionManager.getString("company_id", "Not Found")
        val calendar = Calendar.getInstance()


        currentDate = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(calendar.time)
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        firstDay = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(calendar.time)

        binding!!.startDateTxt.setText(firstDay)
        binding!!.endDateTxt.setText(currentDate)

        binding!!.startDate.setOnClickListener {
            showDatePicker(_binding!!.startDateTxt)
        }

        binding!!.endDateTxt.setOnClickListener {
            showDatePicker(_binding!!.startDateTxt)
        }
        binding!!.btnGo.setOnClickListener {
            loadTripListAPI()
        }

        _binding!!.menuBar.setOnClickListener {
            (activity as? MainActivity)?.toggleDrawer()

        }

    }

    override fun onResume() {
        super.onResume()
        loadTripListAPI()
        getTripList()
    }

    private fun getTripList() {
        lifecycleScope.launch {
            tripListViewModel.uiState.collect { cardetails ->
                when (cardetails) {
                    is UiState.Error -> {
                        binding.noData.visibility = View.VISIBLE
                        binding.recyclerViewTriplist.visibility = View.GONE
                    }

                    UiState.Idle -> {

                    }

                    UiState.Loading -> {}
                    is UiState.Success -> {
                        val response = cardetails.data as ResponseTripList
                        if (response != null) {
                            binding.noData.visibility = View.GONE
                            binding.recyclerViewTriplist.visibility = View.VISIBLE
                            if (response.trip_details.status == 1) {
                                binding.recyclerViewTriplist.layoutManager =
                                    LinearLayoutManager(
                                        requireActivity(),
                                        LinearLayoutManager.VERTICAL,
                                        false
                                    )

                                val adapter =
                                    TripListAdapter(
                                        this@TripListFragment,
                                        response.trip_details.result
                                    )
                                binding.recyclerViewTriplist.adapter = adapter
                            }


                        } else {
                            binding.noData.visibility = View.VISIBLE
                            binding.recyclerViewTriplist.visibility = View.GONE
                        }
                    }
                }
            }
        }
    }

    private fun loadTripListAPI() {
        lifecycleScope.launch {
            val request = RequestTripList(
                company_id = company_id,
                start_date = binding!!.startDateTxt.text.toString(),
                end_date = binding!!.endDateTxt.text.toString(),
                travel_status = "",
                limit = "",
                offset = "",
                search = "",

                )
            tripListViewModel.getDriverDetails(request)
        }
    }

    override fun showTripDetails(_category: ResponseTripList.TripDetails.Result) {
        sessionManager.saveString("trip_id", _category._id.toString())
        findNavController().navigate(R.id.tripDetailsFragment)
    }
}