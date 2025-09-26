package com.blueventor.menu.driverdetailsdashboard

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.blueventor.R
import com.blueventor.databinding.FragmentDriverInfoBinding
import com.blueventor.databinding.FragmentDriverPersonalInfoBinding
import com.blueventor.session.SessionManager
import com.blueventor.util.loadImage
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DriverPersonalInfoFragment : Fragment() {
    private val binding get() = _binding!!
    var driver_licence = ""
    var driver_licence_back_side = ""
    var taxi_image = ""
    var vehicle_registration_license = ""
    @Inject
    lateinit var sessionManager: SessionManager
    private var _binding: FragmentDriverPersonalInfoBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentDriverPersonalInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        driver_licence = sessionManager.getString("driver_licence", "Not Found")
        driver_licence_back_side = sessionManager.getString("driver_licence_back_side", "Not Found")
        taxi_image = sessionManager.getString("taxi_image", "Not Found")
        vehicle_registration_license = sessionManager.getString("vehicle_registration_license", "Not Found")

        binding.image1.loadImage(
            url = driver_licence,
            placeholder = R.drawable.ic_car_placeholder,
            onSuccess = { Log.d("Glide", "Image loaded successfully!") },
            onError = { Log.e("Glide", "Failed to load image") }
        )


        binding.image2.loadImage(
            url = driver_licence_back_side,
            placeholder = R.drawable.ic_car_placeholder,
            onSuccess = { Log.d("Glide", "Image loaded successfully!") },
            onError = { Log.e("Glide", "Failed to load image") }
        )


        binding.image3.loadImage(
            url = taxi_image,
            placeholder = R.drawable.ic_car_placeholder,
            onSuccess = { Log.d("Glide", "Image loaded successfully!") },
            onError = { Log.e("Glide", "Failed to load image") }
        )


        binding.image4.loadImage(
            url = vehicle_registration_license,
            placeholder = R.drawable.ic_car_placeholder   ,
            onSuccess = { Log.d("Glide", "Image loaded successfully!") },
            onError = { Log.e("Glide", "Failed to load image") }
        )




    }
}