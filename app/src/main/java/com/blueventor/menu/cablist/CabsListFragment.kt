package com.blueventor.menu.cablist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.blueventor.R
import com.blueventor.databinding.FragmentCabsListBinding
import com.blueventor.databinding.FragmentDriverDetailsBinding
import com.blueventor.menu.driverlist.DriverListAdapter
import com.blueventor.network.UiState
import com.blueventor.network.request.RequestDashBoardCarPerformanceDetails
import com.blueventor.network.response.ResponseCarPerformance
import com.blueventor.session.SessionManager
import com.blueventor.viewmodel.DashboardViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class CabsListFragment : Fragment() {
    @Inject
    lateinit var sessionManager: SessionManager
    private var _binding: FragmentCabsListBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    val dashboardViewModel: DashboardViewModel by viewModels()
    var company_id = ""
    var currentDate = ""
    var firstDay = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        company_id = sessionManager.getString("company_id", "Not Found")
        val calendar = Calendar.getInstance()


        currentDate = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(calendar.time)
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        firstDay = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(calendar.time)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCabsListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        loadCarpermancedetails()
        getCarPerformanceDetails()
    }

    private fun loadCarpermancedetails() {
        lifecycleScope.launch {
            val request = RequestDashBoardCarPerformanceDetails(
                company_id = company_id, start_date = firstDay,
                end_date = currentDate,
                limit = "10",
                offset = "0",
                search = "",

                )
            dashboardViewModel.getDashBoardCarPerformanceDetails(request)
        }
    }

    private fun getCarPerformanceDetails() {
        lifecycleScope.launch {
            dashboardViewModel.uiStateCarPerformance.collect { cardetails ->
                when (cardetails) {
                    is UiState.Error -> {}
                    UiState.Idle -> {

                    }

                    UiState.Loading -> {}
                    is UiState.Success -> {
                        val response = cardetails.data as ResponseCarPerformance
                        if (response != null) {
                            println("Carpermonse_view" + " " + "djosajdklaskldaskl")
                            binding.recyclerViewCabs.layoutManager =
                                LinearLayoutManager(
                                    requireActivity(),
                                    LinearLayoutManager.VERTICAL,
                                    false
                                )

                            val adapter =
                                CabListAdapter( response.data)
                            binding.recyclerViewCabs.adapter = adapter

                        }
                    }
                }
            }
        }
    }

}