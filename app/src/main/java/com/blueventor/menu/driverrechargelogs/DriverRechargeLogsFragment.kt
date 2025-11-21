package com.blueventor.menu.driverrechargelogs

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
import com.blueventor.databinding.FragmentAllDriversListBinding
import com.blueventor.databinding.FragmentDriverRechargeLogsBinding
import com.blueventor.databinding.FragmentSIngleDriverTripListBinding
import com.blueventor.menu.MainActivity
import com.blueventor.menu.driver.alldriverslist.AllDriversListFragment.AllDriverLifecycleobserver
import com.blueventor.menu.triplist.TripListAdapter
import com.blueventor.network.UiState
import com.blueventor.network.request.RequestDriverRechargeLogs
import com.blueventor.network.request.RequestTripList
import com.blueventor.network.response.ResponseDriverWalletLogs
import com.blueventor.network.response.ResponseDriverWalletRecharge
import com.blueventor.network.response.ResponseTripList
import com.blueventor.network.viewmodel.AllDriverListViewModel
import com.blueventor.network.viewmodel.DriverRechargeLogViewModel
import com.blueventor.session.SessionManager
import com.blueventor.util.onclik
import com.blueventor.util.showDatePicker
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject
import kotlin.getValue

@AndroidEntryPoint
class DriverRechargeLogsFragment : Fragment() {
    @Inject
    lateinit var sessionManager: SessionManager
    lateinit var _binding: FragmentDriverRechargeLogsBinding
    private val binding get() = _binding!!

    val tripDetailsViewModel: DriverRechargeLogViewModel by viewModels()

    private var companyId = ""
    private var tripId = ""
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
        _binding = FragmentDriverRechargeLogsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        company_id = sessionManager.getString("company_id", "Not Found")
        val calendar = Calendar.getInstance()


        currentDate = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(calendar.time)
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        firstDay = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(calendar.time)
        binding.addAmount.setOnClickListener {
            findNavController().navigate(R.id.driverRechargeFragment)
        }
        binding.backIcon.onclik {
            requireActivity().onBackPressed()
        }

        binding!!.startDate.setText(firstDay)
        binding!!.endDate.setText(currentDate)

        binding!!.startDate.setOnClickListener {
            showDatePicker(_binding!!.startDate)
        }

        binding!!.endDate.setOnClickListener {
            showDatePicker(_binding!!.endDate)
        }
        binding!!.dateFlitter.setOnClickListener {
            loadTripListAPI()
        }

//        _binding!!.menuBar.setOnClickListener {
//            (activity as? MainActivity)?.toggleDrawer()
//
//        }

    }

    override fun onResume() {
        super.onResume()
        loadTripListAPI()
        getTripList()
    }

    private fun getTripList() {
        lifecycleScope.launch {
            tripDetailsViewModel.uiState.collect { cardetails ->
                when (cardetails) {
                    is UiState.Error -> {
//                        binding.noData.visibility = View.VISIBLE
//                        binding.recyclerViewTriplist.visibility = View.GONE
                    }

                    UiState.Idle -> {

                    }

                    UiState.Loading -> {}
                    is UiState.Success -> {
                        val response = cardetails.data as ResponseDriverWalletLogs
                        if (response != null) {
                            //   binding.noData.visibility = View.GONE
                           // binding.walletRecycler.visibility = View.VISIBLE
                            if (response.status == 1) {

                                binding.walletRecycler.visibility = View.VISIBLE
                                binding.walletRecycler.layoutManager = LinearLayoutManager(
                                    requireActivity(),
                                    LinearLayoutManager.VERTICAL,
                                    false
                                )

                                // FIX: Prevent index crash
                                if (response.data.records.isNotEmpty()) {
                                    binding.balanceTxt.text = "₹ ${response.data.records[0].account_balance}"
                                } else {
                                    binding.balanceTxt.text = "₹ 0.00"
                                }

                                val adapter = DriverRechargeLogAdapter(response.data.records)
                                binding.walletRecycler.adapter = adapter
                            }



                        } else {
//                            binding.noData.visibility = View.VISIBLE
//                            binding.recyclerViewTriplist.visibility = View.GONE
                        }
                    }
                }
            }
        }
    }

    private fun loadTripListAPI() {
        lifecycleScope.launch {
            val request = RequestDriverRechargeLogs(

                start_date = binding!!.startDate.text.toString(),
                end_date = binding!!.endDate.text.toString(),
                limit = "100",
                offset = "0",
                driver_id = driver_id
            )
            tripDetailsViewModel.getDriverDetails(request)
        }
    }


}