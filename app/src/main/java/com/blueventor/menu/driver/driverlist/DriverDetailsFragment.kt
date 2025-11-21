package com.blueventor.menu.driver.driverlist

import android.content.Intent
import android.net.Uri
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
import com.blueventor.menu.MainActivity
import com.blueventor.network.UiState
import com.blueventor.network.request.RequestDashBoardCarPerformanceDetails
import com.blueventor.network.response.ResponseCarPerformance
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
class DriverDetailsFragment : Fragment(), ShowDriverDetails {
    @Inject
    lateinit var sessionManager: SessionManager
    private var _binding: FragmentDriverDetailsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    val dashboardViewModel: DashboardViewModel by viewModels()
    var company_id = ""
    var currentDate = ""
    var firstDay = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        company_id = sessionManager.getString("company_id", "Not Found")
        val calendar = Calendar.getInstance()


        currentDate = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(calendar.time)
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        firstDay = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(calendar.time)

        binding!!.startDateTxt.setText(currentDate)
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
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentDriverDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun loadCarpermancedetails() {
        lifecycleScope.launch {
            val request = RequestDashBoardCarPerformanceDetails(
                company_id = company_id, start_date = binding!!.startDateTxt.text.toString(),
                end_date =  binding!!.endDateTxt.text.toString(),
                limit = "10",
                offset = "0",
                search = "",

                )
            dashboardViewModel.getDashBoardCarPerformanceDetails(request)
        }
    }

    private fun loadCarpermancedetailsfirst() {
        lifecycleScope.launch {
            val request = RequestDashBoardCarPerformanceDetails(
                company_id = company_id,
                start_date = binding!!.endDateTxt.text.toString(),
                end_date =  binding!!.endDateTxt.text.toString(),
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
                    is UiState.Error -> {

                        binding.noData.visibility = View.VISIBLE
                        binding.recyclerViewDrivers.visibility = View.GONE
                    }
                    UiState.Idle -> {

                    }

                    UiState.Loading -> {}
                    is UiState.Success -> {
                        val response = cardetails.data as ResponseCarPerformance
                        if (response != null) {
                            if(response.status==1)
                            {
                                binding.recyclerViewDrivers.visibility = View.VISIBLE
                                binding.noData.visibility = View.GONE
                                binding.recyclerViewDrivers.layoutManager =
                                    LinearLayoutManager(
                                        requireActivity(),
                                        LinearLayoutManager.VERTICAL,
                                        false
                                    )

                                val adapter =
                                    DriverListAdapter(this@DriverDetailsFragment, response.data)
                                binding.recyclerViewDrivers.adapter = adapter
                            }
                            else{
                                binding.noData.visibility = View.VISIBLE
                                binding.recyclerViewDrivers.visibility = View.GONE
                            }



                        }
                    }
                }
            }
        }
    }



    override fun showDriverDetails(data: ResponseCarPerformance.Data) {
        sessionManager.saveString("driver_id",data.driver_id.toString())
        findNavController().navigate(R.id.driveDashBoardFragment)
    }

    override fun callDriver(responseData: ResponseCarPerformance.Data) {
        val phoneNumber = responseData.driver_phone // replace with dynamic number if needed
        val intent = Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse("tel:$phoneNumber")
        }
        startActivity(intent)
    }

    override fun callMessage(responseData: ResponseCarPerformance.Data) {

        val num = responseData.country_code+responseData.driver_phone // user phone from list
        val uri = Uri.parse("https://wa.me/${num.replace("+", "")}")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        intent.setPackage("com.whatsapp")

        if (intent.resolveActivity(requireActivity().packageManager) != null) {
            requireActivity().startActivity(intent)
        } else {
            requireActivity().startActivity(Intent(Intent.ACTION_VIEW, uri))

        }
    }




    override fun onResume() {
        super.onResume()
        loadCarpermancedetailsfirst()
        getCarPerformanceDetails()
    }

}