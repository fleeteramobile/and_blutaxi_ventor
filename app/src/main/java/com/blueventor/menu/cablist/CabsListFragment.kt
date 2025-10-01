package com.blueventor.menu.cablist

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
import com.blueventor.databinding.FragmentDriverDetailsBinding
import com.blueventor.menu.MainActivity
import com.blueventor.menu.driver.driverlist.DriverListAdapter
import com.blueventor.network.UiState
import com.blueventor.network.request.RequestDashBoardCarPerformanceDetails
import com.blueventor.network.request.RequestGetDriverLocation
import com.blueventor.network.response.ResponseCarList
import com.blueventor.network.response.ResponseCarPerformance
import com.blueventor.network.viewmodel.CarListListViewModel
import com.blueventor.session.SessionManager
import com.blueventor.util.showDatePicker
import com.blueventor.network.viewmodel.DashboardViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class CabsListFragment : Fragment(),ShowCarDetails {
    @Inject
    lateinit var sessionManager: SessionManager
    private var _binding: FragmentCabsListBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    val carListListViewModel:CarListListViewModel by viewModels()
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
        _binding = FragmentCabsListBinding.inflate(inflater, container, false)
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
            loadCarpermancedetails()
        }

        _binding!!.menuBar.setOnClickListener {
            (activity as? MainActivity)?.toggleDrawer()

        }

    }
    override fun onResume() {
        super.onResume()
        loadCarpermancedetails()
        getCarPerformanceDetails()
    }

    private fun loadCarpermancedetails() {
        lifecycleScope.launch {
            val request = RequestGetDriverLocation(
                company_id = company_id


                )
            carListListViewModel.getDriverDetails(request)
        }
    }

    private fun getCarPerformanceDetails() {
        lifecycleScope.launch {
            carListListViewModel.uiState.collect { cardetails ->
                when (cardetails) {
                    is UiState.Error -> {
                        binding.noData.visibility = View.VISIBLE
                        binding.recyclerViewCabs.visibility = View.GONE
                    }
                    UiState.Idle -> {

                    }

                    UiState.Loading -> {}
                    is UiState.Success -> {
                        val response = cardetails.data as ResponseCarList
                        if (response != null) {
                            if (response.status ==1 ) {
                                binding.noData.visibility = View.GONE
                                binding.recyclerViewCabs.visibility = View.VISIBLE
                                binding.recyclerViewCabs.layoutManager =
                                    LinearLayoutManager(
                                        requireActivity(),
                                        LinearLayoutManager.VERTICAL,
                                        false
                                    )

                                val adapter =
                                    CabListAdapter(this@CabsListFragment,response.data)
                                binding.recyclerViewCabs.adapter = adapter
                            }
                            else{
                                binding.noData.visibility = View.VISIBLE
                                binding.recyclerViewCabs.visibility = View.GONE
                            }
                        }
                    }
                }
            }
        }
    }

    override fun showCarDetails(data: ResponseCarList.Data) {
        sessionManager.saveString("driver_name",data.driver_name)
        sessionManager.saveString("driver_phone",data.driver_phone)
        sessionManager.saveString("taxi_insurance_number",data.taxi_insurance_number)
        sessionManager.saveString("taxi_insurance_expire_date_time",data.taxi_insurance_expire_date_time)
        sessionManager.saveString("taxi_fc_expiry_date",data.taxi_fc_expiry_date)
        sessionManager.saveString("taxi_permit_expiry_date",data.taxi_permit_expiry_date)
        sessionManager.saveString("mapping_startdate",data.mapping_startdate)
        sessionManager.saveString("mapping_enddate",data.mapping_enddate)
       findNavController().navigate(R.id.cabDetailsFragment)
    }

}