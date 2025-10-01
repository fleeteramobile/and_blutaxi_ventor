package com.blueventor.menu.cablist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.blueventor.R
import com.blueventor.databinding.FragmentCabDetailsBinding
import com.blueventor.databinding.FragmentCabsListBinding
import com.blueventor.session.SessionManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CabDetailsFragment : Fragment() {
    @Inject
    lateinit var sessionManager: SessionManager
    private var _binding: FragmentCabDetailsBinding? = null
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCabDetailsBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val driverName = sessionManager.getString("driver_name", "Not Found")
        val driverPhone = sessionManager.getString("driver_phone", "Not Found")
        val insuranceNumber = sessionManager.getString("taxi_insurance_number", "Not Found")
        val insuranceExpiry = sessionManager.getString("taxi_insurance_expire_date_time", "Not Found")
        val fcExpiry = sessionManager.getString("taxi_fc_expiry_date", "Not Found")
        val permitExpiry = sessionManager.getString("taxi_permit_expiry_date", "Not Found")
        val mappingStart = sessionManager.getString("mapping_startdate", "Not Found")
        val mappingEnd = sessionManager.getString("mapping_enddate", "Not Found")

        binding.etDriverName.setText(driverName)
        binding.etDriverNumber.setText(driverPhone)
        binding.etInsuranceNumbe.setText(insuranceNumber)
        binding.etInsuranceExpireDateTime.setText(insuranceExpiry)
        binding.etTaxiFcExpiryDate.setText(fcExpiry)
        binding.etTaxiPermitExpiryDate.setText(permitExpiry)
        binding.etMappingStartdate.setText(mappingStart)
        binding.etMappingEnddate.setText(mappingEnd)


    }


}