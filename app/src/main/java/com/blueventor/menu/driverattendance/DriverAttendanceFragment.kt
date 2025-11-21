package com.blueventor.menu.driverattendance

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.LinearSnapHelper
import com.blueventor.R
import com.blueventor.databinding.FragmentCabsListBinding
import com.blueventor.databinding.FragmentDriverAttendanceBinding
import com.blueventor.menu.triplist.TripListAdapter
import com.blueventor.network.UiState
import com.blueventor.network.request.RequestDriverAttendanceDetails
import com.blueventor.network.request.RequestDriverDetails
import com.blueventor.network.response.ResponseDriverAttendanceDetails
import com.blueventor.network.response.ResponseDriverDetails
import com.blueventor.network.viewmodel.CarListListViewModel
import com.blueventor.network.viewmodel.DriverAttendanceDetailsViewModel
import com.blueventor.session.SessionManager
import com.blueventor.util.logDebugMessage
import com.blueventor.util.onclik
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject
import kotlin.getValue

@AndroidEntryPoint
class DriverAttendanceFragment : Fragment() {
    private lateinit var monthAdapter: MonthAdapter
    private val monthList = mutableListOf<MonthModel>()
    @Inject
    lateinit var sessionManager: SessionManager
    private var _binding: FragmentDriverAttendanceBinding? = null
    @RequiresApi(Build.VERSION_CODES.O)
    private var currentYear = LocalDate.now().year
    private val binding get() = _binding!!
    val driverAttendanceDetailViewModel: DriverAttendanceDetailsViewModel by viewModels()
    var company_id = ""
    var driver_id = ""
    var selected_Month = ""
    var selected_year = ""

    private val currentMonthIndex = LocalDate.now().monthValue - 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        company_id = sessionManager.getString("company_id", "Not Found")
        driver_id = sessionManager.getString("driver_id", "Not Found")

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentDriverAttendanceBinding.inflate(inflater, container, false)
        return binding.root    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        generateMonthList()

        monthAdapter = MonthAdapter(monthList) { item ->
            val formattedMonth = String.format("%02d", item.monthNumber)
            selected_Month = formattedMonth
            loadDriverDetaislAPI()

        }


        val layoutManager = LinearLayoutManager(
            requireActivity(),
            LinearLayoutManager.HORIZONTAL,
            false
        )

        _binding!!.monthRecycler.layoutManager = layoutManager
        _binding!!.monthRecycler.adapter = monthAdapter

        // ⭐ SNAP HELPER for Center Alignment
        val snapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(_binding!!.monthRecycler)

        binding.backIcon.onclik {
            requireActivity().onBackPressed()
        }

        // ⭐ SMOOTH SCROLL to center current month
        _binding!!.monthRecycler.post {
            val smoothScroller = object : LinearSmoothScroller(requireContext()) {
                override fun getHorizontalSnapPreference(): Int {
                    return SNAP_TO_START
                }
            }
            smoothScroller.targetPosition = currentMonthIndex
            layoutManager.startSmoothScroll(smoothScroller)
        }

        // YEAR TEXT
        _binding!!.txtYear.text = currentYear.toString()

        _binding!!.btnPrevYear.setOnClickListener {
            currentYear--
            updateYearUI()
        }

        _binding!!.btnNextYear.setOnClickListener {
            currentYear++
            updateYearUI()
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun updateYearUI() {
        _binding!!.txtYear.text = currentYear.toString()
        scrollToMonth(0)

    }

    private fun scrollToMonth(position: Int) {
        val layoutManager = _binding!!.monthRecycler.layoutManager as LinearLayoutManager

        val smoothScroller = object : LinearSmoothScroller(requireContext()) {
            override fun getHorizontalSnapPreference(): Int {
                return SNAP_TO_START
            }
        }

        smoothScroller.targetPosition = position
        layoutManager.startSmoothScroll(smoothScroller)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun generateMonthList() {
        val months = listOf(
            "Jan", "Feb", "Mar", "Apr", "May", "Jun",
            "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
        )

        monthList.clear()

        val currentMonth = LocalDate.now().monthValue   // <-- CURRENT MONTH

        for (i in 1..12) {
            monthList.add(
                MonthModel(
                    name = months[i - 1],
                    monthNumber = i,
                    isSelected = (i == currentMonth)   // <-- HIGHLIGHT
                )
            )
        }
    }

    override fun onResume() {
        super.onResume()
        loadDriverDetaislAPIFirst()
        getDrierDetailsDetails()
    }

    private fun loadDriverDetaislAPIFirst() {
         val currentMonthFormatted = String.format("%02d", LocalDate.now().monthValue)

        lifecycleScope.launch {
            val request = RequestDriverAttendanceDetails(
                company_id = company_id,
                driver_id = driver_id,
                month = currentMonthFormatted,
                year =_binding!!.txtYear.text.toString()

            )
            driverAttendanceDetailViewModel.getDriverDetails(request)
        }
    }
 private fun loadDriverDetaislAPI() {
        lifecycleScope.launch {
            val request = RequestDriverAttendanceDetails(
                company_id = company_id,
                driver_id = driver_id,
                month = selected_Month,
                year = _binding!!.txtYear.text.toString()

            )
            driverAttendanceDetailViewModel.getDriverDetails(request)
        }
    }

    private fun getDrierDetailsDetails() {
        lifecycleScope.launch {
            driverAttendanceDetailViewModel.uiState.collect { cardetails ->
                when (cardetails) {
                    is UiState.Error -> {
                        logDebugMessage("getdriverdetails", cardetails.message)
                    }

                    UiState.Idle -> {

                    }

                    UiState.Loading -> {}
                    is UiState.Success -> {
                        val response = cardetails.data as ResponseDriverAttendanceDetails
                        if (response != null) {
                            binding.averageLoginTime.setText(response.details.average_login_time)
                            binding.totalLoginTime.setText(response.details.total_login_time)
                            binding.recyclerAttanceDetails.layoutManager =
                                LinearLayoutManager(
                                    requireActivity(),
                                    LinearLayoutManager.VERTICAL,
                                    false
                                )

                            val adapter =
                                DriverAttendanceLogAdapter(
                                    response.details.attendance_details
                                )
                            binding.recyclerAttanceDetails.adapter = adapter
                        }
                    }
                }
            }
        }
    }

}