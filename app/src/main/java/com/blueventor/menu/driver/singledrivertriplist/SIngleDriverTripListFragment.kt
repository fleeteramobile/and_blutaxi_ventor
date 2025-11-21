package com.blueventor.menu.driver.singledrivertriplist

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
import com.blueventor.databinding.FragmentSIngleDriverTripListBinding
import com.blueventor.databinding.FragmentTripListBinding
import com.blueventor.menu.MainActivity
import com.blueventor.menu.triplist.ShowTripDetails
import com.blueventor.menu.triplist.TripListAdapter
import com.blueventor.network.UiState
import com.blueventor.network.request.RequestTripList
import com.blueventor.network.response.ResponseTripList
import com.blueventor.network.viewmodel.TripListViewModel
import com.blueventor.session.SessionManager
import com.blueventor.util.showDatePicker
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject
import kotlin.getValue

@AndroidEntryPoint
class SIngleDriverTripListFragment : Fragment(), ShowTripDetails {
    @Inject
    lateinit var sessionManager: SessionManager
    private var _binding: FragmentSIngleDriverTripListBinding? = null
    private val binding get() = _binding!!
    val tripListViewModel: TripListViewModel by viewModels()
    var company_id = ""
    var driver_id = ""
    var currentDate = ""
    var firstDay = ""
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
        _binding = FragmentSIngleDriverTripListBinding.inflate(inflater, container, false)
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
                                        this@SIngleDriverTripListFragment,
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
                driver_id= driver_id
                )
            tripListViewModel.getDriverDetails(request)
        }
    }

    override fun showTripDetails(_category: ResponseTripList.TripDetails.Result) {
        sessionManager.saveString("trip_id", _category._id.toString())
        findNavController().navigate(R.id.tripDetailsFragment)
    }
}