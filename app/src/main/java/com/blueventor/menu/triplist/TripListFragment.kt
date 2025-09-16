package com.blueventor.menu.triplist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.blueventor.R
import com.blueventor.databinding.FragmentCabsListBinding
import com.blueventor.databinding.FragmentTripListBinding
import com.blueventor.session.SessionManager
import com.blueventor.viewmodel.DashboardViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject


class TripListFragment : Fragment() {
    @Inject
    lateinit var sessionManager: SessionManager
    private var _binding: FragmentTripListBinding? = null

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
        _binding = FragmentTripListBinding.inflate(inflater, container, false)
        return binding.root
    }


}