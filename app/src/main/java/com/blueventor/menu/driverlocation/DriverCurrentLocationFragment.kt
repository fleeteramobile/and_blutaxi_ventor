package com.blueventor.menu.driverlocation

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.location.Location
import android.os.Bundle
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.DrawableRes
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.blueventor.R
import com.blueventor.databinding.FragmentDriveDashBoardBinding
import com.blueventor.databinding.FragmentDriverCurrentLocationBinding
import com.blueventor.network.UiState
import com.blueventor.network.request.RequestGetDriverLocation
import com.blueventor.network.request.RequestTripDetails
import com.blueventor.network.response.ResponseGetDriverLocation
import com.blueventor.network.response.ResponseTripDetails
import com.blueventor.network.viewmodel.DriverCurrentLocationViewModel
import com.blueventor.network.viewmodel.TripDetailsViewModel
import com.blueventor.session.SessionManager
import com.blueventor.util.logDebugMessage
import com.blueventor.util.onclik
import com.blueventor.util.showAlert
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class DriverCurrentLocationFragment : Fragment(), OnMapReadyCallback {
    val driverCurrentLocationViewModel: DriverCurrentLocationViewModel by viewModels()
    var company_id = ""
    private val locationPermissionRequest =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            if (permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true || permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true) {
                locationPermissionGranted = true   // <-- Add this line
                showCurrentLocationOnMap()
            } else {
                Toast.makeText(requireContext(), "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }


    lateinit var _binding: FragmentDriverCurrentLocationBinding
    private val driverMarkers = mutableListOf<Marker>()

    @Inject
    lateinit var sessionManager: SessionManager
    private val binding get() = _binding!!
    private lateinit var googleMap: GoogleMap

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var locationPermissionGranted = false
    private var lastKnownLocation: Location? = null
    private val defaultLocation = LatLng(-33.8523341, 151.2106085)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentDriverCurrentLocationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        company_id = sessionManager.getString("company_id", "Not Found")
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
        _binding.mapBack.onclik {
            requireActivity().onBackPressed()
        }
    }


    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        requestLocationPermission()
    }


    // Call this to request permissions
    fun requestLocationPermission() {
        locationPermissionRequest.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }


    @SuppressLint("MissingPermission")
    private fun showCurrentLocationOnMap() {
        if (!locationPermissionGranted) return

        fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                val currentLatLng = LatLng(location.latitude, location.longitude)

                logDebugMessage("currentLatLng", currentLatLng.toString())

                googleMap.isMyLocationEnabled = true
                googleMap.uiSettings.isMyLocationButtonEnabled = true

                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 16f))

//                googleMap.addMarker(
//                    MarkerOptions().position(currentLatLng).title("You are here")
//                )
            } else {
                // Request fresh location update if lastLocation is null
                val locationRequest =
                    LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 1000).build()
                fusedLocationProviderClient.requestLocationUpdates(
                    locationRequest, object : LocationCallback() {
                        override fun onLocationResult(result: LocationResult) {
                            fusedLocationProviderClient.removeLocationUpdates(this)
                            result.lastLocation?.let {
                                val currentLatLng = LatLng(it.latitude, it.longitude)
                                googleMap.animateCamera(
                                    CameraUpdateFactory.newLatLngZoom(
                                        currentLatLng, 16f
                                    )
                                )
                                // googleMap.addMarker(MarkerOptions().position(currentLatLng).title("You are here"))
                            }
                        }
                    }, Looper.getMainLooper()
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
        callCurrentLocation()
        getDriverCurretLocation()
    }

    private fun getDriverCurretLocation() {
        lifecycleScope.launch {
            driverCurrentLocationViewModel.uiState.collect { cardetails ->
                when (cardetails) {
                    is UiState.Error -> {
                        requireActivity().showAlert("No drivers in online")
                    }

                    UiState.Idle -> {

                    }

                    UiState.Loading -> {}
                    is UiState.Success -> {
                        val response = cardetails.data as ResponseGetDriverLocation
                        if (response != null && response.status == 1) {
                            val drivers = response.data
                            if (!drivers.isNullOrEmpty()) {
                                updateDriverMarkers(drivers)
                            } else {
                                requireActivity().showAlert("No drivers online")
                            }
                        } else {
                            requireActivity().showAlert("No drivers online")
                        }

                    }
                }
            }
        }
    }

    private fun updateDriverMarkers(drivers: List<ResponseGetDriverLocation.Data>) {
        // Clear existing markers
        googleMap.clear()
        driverMarkers.clear()

        drivers.forEach { driver ->
            val lat = driver.loc.coordinates[1]
            val lng = driver.loc.coordinates[0]
            val driverLatLng = LatLng(lat, lng)

            val markerIcon = when (driver.driver_status) {
                "F" -> getBitmapDescriptorFromDrawable(R.drawable.free)    // Green car
                "B" -> getBitmapDescriptorFromDrawable(R.drawable.busy)    // Blue car
                "A" -> getBitmapDescriptorFromDrawable(R.drawable.active)  // Red car
                else -> getBitmapDescriptorFromDrawable(R.drawable.busy)
            }

            val marker = googleMap.addMarker(
                MarkerOptions()
                    .position(driverLatLng)
                    .icon(markerIcon)   // <-- USE IT DIRECTLY
            )

            marker?.tag = driver
            marker?.let { driverMarkers.add(it) }
        }


        // Show all markers within camera
        if (drivers.isNotEmpty()) {
            val builder = LatLngBounds.Builder()
            drivers.forEach {
                builder.include(LatLng(it.loc.coordinates[1], it.loc.coordinates[0]))
            }
            val bounds = builder.build()
            googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100))
        }

        // Set custom info window
        googleMap.setInfoWindowAdapter(object : GoogleMap.InfoWindowAdapter {
            override fun getInfoWindow(marker: Marker): View? = null // Use default frame

            override fun getInfoContents(marker: Marker): View {
                val view = layoutInflater.inflate(R.layout.custom_info_window, null)
                val driver = marker.tag as? ResponseGetDriverLocation.Data
                driver?.let {
                    view.findViewById<TextView>(R.id.tvDriverCode).text = "Driver Code: ${it.driver_code}"
                    view.findViewById<TextView>(R.id.tvMobile).text = "Mobile: ${it.country_code}${it.driver_mobile}"
                    view.findViewById<TextView>(R.id.tvTaxiNo).text = "Taxi No: ${it.taxi_no}"
                    view.findViewById<TextView>(R.id.tvTaxiModel).text = "Taxi Model: ${it.taxi_model}"
                    if (it.trip_type.equals(""))
                    {
                        view.findViewById<TextView>(R.id.trip_type).visibility = View.GONE

                    }
                    else{
                        view.findViewById<TextView>(R.id.trip_type).visibility = View.VISIBLE
                        view.findViewById<TextView>(R.id.trip_type).text = "Trip Type: ${it.trip_type}"

                    }
                }
                return view
            }
        })
    }

    private fun getBitmapDescriptorFromDrawable(@DrawableRes drawableId: Int): BitmapDescriptor {
        val drawable = ContextCompat.getDrawable(requireActivity(), drawableId)!!
        val bitmap = Bitmap.createBitmap(
            drawable.intrinsicWidth,
            drawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }



    private fun callCurrentLocation() {
        lifecycleScope.launch {
            val request = RequestGetDriverLocation(
                company_id = company_id,


                )
            driverCurrentLocationViewModel.getDriverDetails(request)
        }
    }
}