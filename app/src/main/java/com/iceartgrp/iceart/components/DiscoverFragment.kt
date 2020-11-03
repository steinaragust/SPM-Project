package com.iceartgrp.iceart.components

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.iceartgrp.iceart.R
import com.iceartgrp.iceart.network.ApiConsumer
import kotlinx.android.synthetic.main.fragment_discover.*
import kotlinx.android.synthetic.main.fragment_discover.loading_spinner
import kotlinx.android.synthetic.main.photo_info_fragment.*

/**
 * A simple [Fragment] subclass.
 * Use the [DiscoverFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DiscoverFragment :
    Fragment(),
    OnMapReadyCallback {
    // TODO: Rename and change types of parameters

    private lateinit var googleMap: GoogleMap
    private var map: GoogleMap? = null
    private var cameraPosition: CameraPosition? = null

    // The entry point to the Fused Location Provider.
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    // A default location (Sydney, Australia) and default zoom to use when location permission is
    // not granted.
    private val defaultLocation = LatLng(64.1329784, -21.9100092)
    private var locationPermissionGranted = false
    private var lastKnownLocation: Location? = null

    private lateinit var markerPerth: Marker

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // Retrieve location and camera position from saved instance state.
        if (savedInstanceState != null) {
            lastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION)
            cameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION)
        }

        // Construct a FusedLocationProviderClient.
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(activity!!)

        // Build the map.
        arguments?.let {
        }
        map_view.onCreate(savedInstanceState)
        map_view.onResume()

        map_view.getMapAsync(this)
    }

    /**
     * Saves the state of the map when the activity is paused.
     */
    override fun onSaveInstanceState(outState: Bundle) {
        map?.let { map ->
            outState.putParcelable(KEY_CAMERA_POSITION, map.cameraPosition)
            outState.putParcelable(KEY_LOCATION, lastKnownLocation)
        }
        super.onSaveInstanceState(outState)
    }

    /**
     * Sets up the options menu.
     * @param menu The options menu.
     * @return Boolean.
     */

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_discover, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment DiscoverFragment.
         */
        private val TAG = DiscoverFragment::class.java.simpleName
        private val DEFAULT_ZOOM = 15
        private val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1

        // Keys for storing activity state.
        private val KEY_CAMERA_POSITION = "camera_position"
        private val KEY_LOCATION = "location"
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            DiscoverFragment()
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(map: GoogleMap) {
        this.map = map

        // Use a custom info window adapter to handle multiple lines of text in the
        // info window contents.
        // Prompt the user for permission.
        getLocationPermission()

        // Turn on the My Location layer and the related control on the map.
        updateLocationUI()

        // Get the current location of the device and set the position of the map.
        getDeviceLocation()
    }

    private fun addExhibitions() {
        ApiConsumer().getNearbyExhibitions(
            lastKnownLocation?.latitude!!,
            lastKnownLocation?.longitude!!,
            onSuccess = { exhibitions ->
                for (ex in exhibitions) {
                    this.map!!.addMarker(MarkerOptions().position(LatLng(ex.latitude.toDouble(), ex.longitude.toDouble())).title(ex.title).snippet(ex.info))
                }
                map_view?.visibility = View.VISIBLE
                loading_spinner?.visibility = View.GONE
            },
            onFailure = { statusCode ->
                var errorMessage = "Something went wrong, please try again later"
                if (statusCode == -1) {
                    errorMessage = "Connection failed, please check your internet connection"
                }
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
                Log.e("Request Failure", statusCode.toString())
            }
        )
    }
    /**
     * Gets the current location of the device, and positions the map's camera.
     */
    private fun getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (locationPermissionGranted) {
                val locationResult = fusedLocationProviderClient.lastLocation
                locationResult.addOnCompleteListener(activity!!) { task ->
                    if (task.isSuccessful) {
                        // Set the map's camera position to the current location of the device.
                        lastKnownLocation = task.result
                        if (lastKnownLocation != null) {
                            map?.moveCamera(
                                CameraUpdateFactory.newLatLngZoom(
                                    LatLng(
                                        lastKnownLocation!!.latitude,
                                        lastKnownLocation!!.longitude
                                    ),
                                    DEFAULT_ZOOM.toFloat()
                                )
                            )
                        }
                        addExhibitions()
                    } else {
                        Log.d(TAG, "Current location is null. Using defaults.")
                        Log.e(TAG, "Exception: %s", task.exception)
                        map?.moveCamera(
                            CameraUpdateFactory
                                .newLatLngZoom(defaultLocation, DEFAULT_ZOOM.toFloat())
                        )
                        map?.uiSettings?.isMyLocationButtonEnabled = false
                    }
                }
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message, e)
        }
    }

    /**
     * Prompts the user for permission to use the device location.
     */
    private fun getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.context!!, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED
        ) {
            locationPermissionGranted = true
        } else {
            ActivityCompat.requestPermissions(this.activity!!, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION)
            fragmentManager?.beginTransaction()?.detach(this)?.attach(this)?.commit()
        }
    }

    /**
     * Handles the result of the request for location permissions.
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        locationPermissionGranted = false
        when (requestCode) {
            PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION -> {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    locationPermissionGranted = true
                }
            }
        }
        updateLocationUI()
    }

    private fun updateLocationUI() {
        if (map == null) {
            return
        }
        try {
            if (locationPermissionGranted) {
                map?.isMyLocationEnabled = true
                map?.uiSettings?.isMyLocationButtonEnabled = true
            } else {
                map?.isMyLocationEnabled = false
                map?.uiSettings?.isMyLocationButtonEnabled = false
                lastKnownLocation = null
                getLocationPermission()
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message, e)
        }
    }

    private fun showCurrentPlace() {
        if (map == null) {
            return
        }
        if (locationPermissionGranted) {
            // Add a default marker
            map?.addMarker(
                MarkerOptions()
                    .title(getString(R.string.default_info_title))
                    .position(defaultLocation)
                    .snippet(getString(R.string.default_info_snippet))
            )
        } else {
            // Prompt the user for permission.
            getLocationPermission()
        }
    }
}
