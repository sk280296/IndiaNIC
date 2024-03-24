package com.app.indianic.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.navigation.fragment.findNavController
import com.app.indianic.R
import com.app.indianic.base.BaseFragment
import com.app.indianic.databinding.FragmentLocationSelectionBinding
import com.app.indianic.utils.changeBackgroundColor
import com.app.indianic.utils.logError
import com.app.indianic.utils.showToast
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.PlacesClient
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class LocationSelectionFragment :
    BaseFragment<FragmentLocationSelectionBinding>(FragmentLocationSelectionBinding::inflate),
    OnMapReadyCallback,
    ActivityCompat.OnRequestPermissionsResultCallback {

    private var googleMap: GoogleMap? = null
    private val LOCATION_PERMISSION_REQUEST_CODE = 1
    private val TAG = this.javaClass.name

    private var mPlacesClient: PlacesClient? = null
    private var mFusedLocationProviderClient: FusedLocationProviderClient? = null

    private var LATITUDE_VALUE = ""
    private var LONGITUDE_VALUE = ""

    private var streetValueFromBundle: String? = ""
    private var cityValueFromBundle: String? = ""
    private var stateValueFromBundle: String? = ""
    private var zipcodeValueFromBundle: String? = ""
    private var countryValueFromBundle: String? = ""
    private var latitudeFromBundle: String? = "0.0"
    private var longitudeFromBundle: String? = "0.0"

    private var showingFromBundle = false


    var currentLocation: LatLng = LatLng(20.5, 78.9)
    private val PERMISSION_ID = 42

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        setListener()
    }


    private fun init() {
        val bundle = arguments
        latitudeFromBundle = bundle?.getString("LATITUDE") ?: "0.0"
        longitudeFromBundle = bundle?.getString("LONGITUDE") ?: "0.0"
        streetValueFromBundle = bundle?.getString("STREET_VALUE") ?: ""
        cityValueFromBundle = bundle?.getString("CITY_VALUE") ?: ""
        stateValueFromBundle = bundle?.getString("STATE_VALUE") ?: ""
        zipcodeValueFromBundle = bundle?.getString("ZIPCODE_VALUE") ?: ""
        countryValueFromBundle = bundle?.getString("COUNTRY_VALUE") ?: ""

        showingFromBundle = !countryValueFromBundle.isNullOrEmpty()

        logError("latitudeFromBundle$latitudeFromBundle")

        try {
            val apiKey = getString(R.string.google_maps_key)
            Places.initialize(requireContext(), apiKey)
            mPlacesClient = Places.createClient(requireActivity())
        } catch (e: Exception) {
            e.printStackTrace()
        }

        val mapFragment = childFragmentManager
            .findFragmentById(R.id.mapFragment) as SupportMapFragment?

        mapFragment!!.getMapAsync(this)

        mFusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())

    }

    private fun setListener() {
        binding?.apply {
            cvContinue.setOnClickListener {
                findNavController().navigate(R.id.weatherDataFragment, Bundle().also {
                    it.putFloat("latitude", currentLocation.latitude.toFloat())
                    it.putFloat("longitude", currentLocation.longitude.toFloat())
                })

            }

            etStreetNumber.setAfterTextChangedListener {
                changeButtonAppearance()
            }
            etCity.setAfterTextChangedListener {
                changeButtonAppearance()
            }
            etState.setAfterTextChangedListener {
                changeButtonAppearance()
            }
            etZipCode.setAfterTextChangedListener {
                changeButtonAppearance()
            }
            etCountry.setAfterTextChangedListener {
                changeButtonAppearance()
            }
            ivCurrentLocation.setOnClickListener {
                getLastLocation()
            }

        }
    }

    private fun isValid(): Boolean {
        val streetNumber = binding?.etStreetNumber?.getText()
        val cityName = binding?.etCity?.getText()
        val stateName = binding?.etState?.getText()
        val zipCode = binding?.etZipCode?.getText()
        val countryName = binding?.etCountry?.getText()
        if (streetNumber?.isNotEmpty() == true && cityName?.isNotEmpty() == true && stateName?.isNotEmpty() == true && zipCode?.isNotEmpty() == true && countryName?.isNotEmpty() == true) {
        } else {
            if (streetNumber?.isEmpty() == true) {
                return false
            } else if (cityName?.isEmpty() == true) {
                return false
            } else if (stateName?.isEmpty() == true) {
                return false
            } else if (zipCode?.isEmpty() == true) {
                return false
            } else if (countryName?.isEmpty() == true) {
                return false
            }

        }
        return true
    }

    private fun changeCardColor(setEnable: Boolean) {
        binding?.cvContinue?.changeBackgroundColor(if (setEnable) R.color.color_E94826 else R.color.color_E94826_50)
    }

    private fun changeButtonAppearance() {
        val isValid = isValid()
        changeCardColor(isValid)
        binding?.cvContinue?.isEnabled = isValid
    }

    override fun onMapReady(it: GoogleMap) {
        googleMap = it

        if (latitudeFromBundle == "0.0") {
            getLastLocation()
        } else {
            val location = LatLng(
                latitudeFromBundle?.toDouble() ?: 0.0,
                longitudeFromBundle?.toDouble() ?: 0.0
            )
            googleMap?.animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                    location,
                    16F
                )
            )
        }

        googleMap?.setOnCameraIdleListener {
            val latLng = googleMap?.cameraPosition?.target
            val currLat = latLng?.latitude
            val currLong = latLng?.longitude
            Log.e(TAG, "currLat: $currLat")
            Log.e(TAG, "currLong: $currLong")
            googleMap?.clear()

            if (showingFromBundle) {
                setAddressFromBundle()
                showingFromBundle = false
            } else {
                if (currLat != null && currLong != null) {
                    setAddressInFields(currLat, currLong)
                }
            }

        }

    }

    private fun setAddressFromBundle() {
        requireActivity().runOnUiThread {
            try {
                binding?.apply {
                    if (!streetValueFromBundle.isNullOrEmpty()) {
                        etStreetNumber.setText(streetValueFromBundle!!)
                        etStreetNumber.changeAppearance(true)
                    }
                    if (!cityValueFromBundle.isNullOrEmpty()) {
                        etCity.setText(cityValueFromBundle!!)
                        etCity.changeAppearance(true)
                    }
                    if (!stateValueFromBundle.isNullOrEmpty()) {
                        etState.setText(stateValueFromBundle!!)
                        etState.changeAppearance(true)
                    }
                    if (!zipcodeValueFromBundle.isNullOrEmpty()) {
                        etZipCode.setText(zipcodeValueFromBundle!!)
                        etZipCode.changeAppearance(true)
                    }
                    if (!countryValueFromBundle.isNullOrEmpty()) {
                        etCountry.setText(countryValueFromBundle!!)
                        etCountry.changeAppearance(true)
                    }
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

    /**
     * Displays a dialog with error message explaining that the location permission is missing.
     */

    // Get current location
    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                googleMap?.isMyLocationEnabled = true
                googleMap?.uiSettings?.isMyLocationButtonEnabled = false
                mFusedLocationProviderClient?.lastLocation?.addOnCompleteListener(requireActivity()) { task ->
                    val location: Location? = task.result
                    if (location == null) {
                        requestNewLocationData()
                    } else {
                        currentLocation = LatLng(location.latitude, location.longitude)
                        googleMap?.clear()
                        googleMap?.animateCamera(
                            CameraUpdateFactory.newLatLngZoom(
                                currentLocation,
                                16F
                            )
                        )
                        setAddressInFields(location.latitude, location.longitude)
                    }
                }
            } else {
                requireActivity().showToast(getString(R.string.turn_on_device_location))
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermissions()
        }
    }

    private fun checkPermissions(): Boolean {
        return ActivityCompat.checkSelfPermission(
            requireActivity(),
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                    requireActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
    }

    // Request permissions if not granted before
    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            PERMISSION_ID
        )
    }

    // function to check if GPS is on
    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    // What must happen when permission is granted
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            return
        }
        if (requestCode == PERMISSION_ID) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                getLastLocation()
            }
        }
    }

    // Get current location, if shifted
    // from previous location
    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {
        val mLocationRequest = LocationRequest.create().apply {
            priority = PRIORITY_HIGH_ACCURACY
            interval = 0
            fastestInterval = 0
            numUpdates = 1
        }

        mFusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())
        mFusedLocationProviderClient?.requestLocationUpdates(
            mLocationRequest, mLocationCallback,
            Looper.myLooper()
        )
    }

    // If current location could not be located, use last location
    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val mLastLocation: Location? = locationResult.lastLocation
            mLastLocation?.let {
                currentLocation = LatLng(mLastLocation.latitude, mLastLocation.longitude)
            }
        }
    }

    private fun setAddressInFields(currLat: Double, currLong: Double) {
        try {
            val geo = Geocoder(
                requireActivity().applicationContext,
                Locale.getDefault()
            )


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                geo.getFromLocation(currLat, currLong, 1) { addresses ->
                    addresses.forEach {
                        logError("CompleteAddress : ${it.toString()}")
                    }
                    if (addresses.isNullOrEmpty()) {
                        requireActivity().runOnUiThread {
                            requireActivity().showToast("Waiting for Location")
                        }
                    } else {
                        if (addresses.size > 0) {
                            requireActivity().runOnUiThread {
                                try {
                                    binding?.apply {
                                        if (!addresses[0].featureName.isNullOrEmpty()) {
                                            etStreetNumber.setText(addresses[0].featureName)
                                            etStreetNumber.changeAppearance(!addresses[0].featureName.isNullOrEmpty())
                                        }
                                        if (!addresses[0].locality.isNullOrEmpty()) {
                                            etCity.setText(addresses[0].locality)
                                            etCity.changeAppearance(!addresses[0].locality.isNullOrEmpty())
                                        }
                                        if (!addresses[0].adminArea.isNullOrEmpty()) {
                                            etState.setText(addresses[0].adminArea)
                                            etState.changeAppearance(!addresses[0].adminArea.isNullOrEmpty())
                                        }
                                        if (!addresses[0].postalCode.isNullOrEmpty()) {
                                            etZipCode.setText(addresses[0].postalCode)
                                            etZipCode.changeAppearance(!addresses[0].postalCode.isNullOrEmpty())
                                        }
                                        if (!addresses[0].countryName.isNullOrEmpty()) {
                                            etCountry.setText(addresses[0].countryName)
                                            etCountry.changeAppearance(!addresses[0].countryName.isNullOrEmpty())
                                        }
                                    }

                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                                LATITUDE_VALUE = addresses[0].latitude.toString()
                                LONGITUDE_VALUE = addresses[0].longitude.toString()

                            }

                        }
                    }
                }
            } else {
                val addresses: List<Address>? =
                    geo.getFromLocation(currLat!!, currLong!!, 1)
                addresses?.let { address ->
                    address.forEach {
                        logError("CompleteAddress : $it")
                    }
                    if (address.isEmpty()) {
                        requireActivity().runOnUiThread {
                            requireActivity().showToast("Waiting for Location")
                        }
                    } else {
                        if (!address.isNullOrEmpty()) {
                            requireActivity().runOnUiThread {
                                try {
                                    binding?.apply {
                                        if (!addresses[0].featureName.isNullOrEmpty()) {
                                            etStreetNumber.setText(address[0].featureName ?: "")
                                            etStreetNumber.changeAppearance(!address[0].featureName.isNullOrEmpty())
                                        }
                                        if (!addresses[0].locality.isNullOrEmpty()) {
                                            etCity.setText(address[0].locality)
                                            etCity.changeAppearance(!address[0].locality.isNullOrEmpty())
                                        }
                                        if (!addresses[0].adminArea.isNullOrEmpty()) {
                                            etState.setText(address[0].adminArea)
                                            etState.changeAppearance(!address[0].adminArea.isNullOrEmpty())
                                        }
                                        if (!addresses[0].postalCode.isNullOrEmpty()) {
                                            etZipCode.setText(address[0].postalCode)
                                            etZipCode.changeAppearance(!address[0].postalCode.isNullOrEmpty())
                                        }
                                        if (!addresses[0].countryName.isNullOrEmpty()) {
                                            etCountry.setText(address[0].countryName)
                                            etCountry.changeAppearance(!address[0].countryName.isNullOrEmpty())
                                        }
                                    }
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }


                            LATITUDE_VALUE = addresses[0].latitude.toString()
                            LONGITUDE_VALUE = addresses[0].longitude.toString()

                        }
                    }
                }

            }

        } catch (e: java.lang.Exception) {
            e.printStackTrace() // getFromLocation() may sometimes fail
        }
    }
}