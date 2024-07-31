package com.gyasilarbi.jobdash

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.io.IOException
import java.util.Locale

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var googleMap: GoogleMap
    private lateinit var locationTextView: TextView
    private lateinit var searchView: SearchView
    private lateinit var saveLocationButton: Button
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private var selectedLatLng: LatLng? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        locationTextView = findViewById(R.id.locationTextView)
        searchView = findViewById(R.id.searchView)
        saveLocationButton = findViewById(R.id.saveLocationButton)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
        mapFragment?.getMapAsync(this)

        setupSearchView()
        setupSaveLocationButton()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap
        if (checkGooglePlayServices()) {
            checkLocationPermission()
        } else {
            showToast("Google Play Services not available")
        }
        setupMapClickListener()
    }

    override fun onResume() {
        super.onResume()
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getCurrentLocation()
        }
    }

    override fun onPause() {
        super.onPause()
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    private fun checkGooglePlayServices(): Boolean {
        val googleApiAvailability = GoogleApiAvailability.getInstance()
        val resultCode = googleApiAvailability.isGooglePlayServicesAvailable(this)
        return resultCode == ConnectionResult.SUCCESS
    }

    private fun checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                showToast("Location permission is needed to show your current location.")
            }
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        } else {
            getCurrentLocation()
        }
    }

    private fun returnLocationResult(latLng: LatLng) {
        val intent = Intent().apply {
            putExtra("latitude", latLng.latitude)
            putExtra("longitude", latLng.longitude)
        }
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getCurrentLocation()
        } else {
            showToast("Location permission denied")
        }
    }

    private fun updateLocationUI(location: LatLng?) {
        location?.let {
            val locationText = "Lat: ${it.latitude}, Lng: ${it.longitude}"
            locationTextView.text = locationText
        } ?: run {
            locationTextView.text = "No location selected"
        }
    }

    private fun getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return
        }

        val locationRequest = LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            interval = 10000
            fastestInterval = 5000
        }

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult.locations.firstOrNull()?.let { location ->
                    val currentLatLng = LatLng(location.latitude, location.longitude)
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f))
                    googleMap.addMarker(MarkerOptions().position(currentLatLng).title("Current Location"))
                    getAddressFromLatLng(currentLatLng)
                    fusedLocationClient.removeLocationUpdates(this)
                }
            }
        }

        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
    }

    private fun setupSearchView() {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { searchLocation(it) }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })
    }

    private fun setupMapClickListener() {
        googleMap.setOnMapClickListener { latLng ->
            googleMap.clear()
            googleMap.addMarker(MarkerOptions().position(latLng).title("Selected Location"))
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
            getAddressFromLatLng(latLng)
            selectedLatLng = latLng
        }
    }

    private fun setupSaveLocationButton() {
        saveLocationButton.setOnClickListener {
            selectedLatLng?.let { latLng ->
                val geocoder = Geocoder(this, Locale.getDefault())
                try {
                    val addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
                    if (addresses != null) {
                        if (addresses.isNotEmpty()) {
                            addresses.firstOrNull()?.let { address ->
                                val addressText = address.getAddressLine(0)
                                val street = address.thoroughfare ?: "Unknown Street"
                                val city = address.locality ?: "Unknown City"
                                val region = address.adminArea ?: "Unknown Region"
                                val country = address.countryName ?: "Unknown Country"
                                val postalCode = address.postalCode ?: "Unknown Postal Code"

                                val resultIntent = Intent().apply {
                                    putExtra("address", addressText)
                                    putExtra("street", street)
                                    putExtra("city", city)
                                    putExtra("region", region)
                                    putExtra("country", country)
                                    putExtra("postalCode", postalCode)
                                }
                                setResult(Activity.RESULT_OK, resultIntent)
                                finish()
                            } ?: run {
                                showToast("Address not found")
                            }
                        } else {
                            showToast("Address not found")
                        }
                    }
                } catch (e: IOException) {
                    handleGeocoderException(e)
                }
            } ?: run {
                showToast("No location selected")
            }
        }
    }


    private fun searchLocation(locationName: String) {
        val geocoder = Geocoder(this, Locale.getDefault())
        try {
            val addresses = geocoder.getFromLocationName(locationName, 1)
            if (addresses != null) {
                if (addresses.isNotEmpty()) {
                    addresses.firstOrNull()?.let { address ->
                        val latLng = LatLng(address.latitude, address.longitude)
                        googleMap.clear()
                        googleMap.addMarker(MarkerOptions().position(latLng).title("Searched Location"))
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
                        locationTextView.text = address.getAddressLine(0)
                        showToast("Location Found: ${address.getAddressLine(0)}")
                    }
                } else {
                    locationTextView.text = "Address not found"
                    showToast("Address not found")
                }
            }
        } catch (e: IOException) {
            handleGeocoderException(e)
        } catch (e: IllegalArgumentException) {
            handleGeocoderException(e)
        }
    }

    private fun getAddressFromLatLng(latLng: LatLng) {
        val geocoder = Geocoder(this, Locale.getDefault())
        try {
            val addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
            if (addresses != null) {
                if (addresses.isNotEmpty()) {
                    addresses.firstOrNull()?.let { address ->
                        val addressText = address.getAddressLine(0)
                        locationTextView.text = addressText
                        showToast("Location Selected: $addressText")
                    }
                } else {
                    locationTextView.text = "Address not found"
                    showToast("Address not found")
                }
            }
        } catch (e: IOException) {
            handleGeocoderException(e)
        }
    }

    private fun handleGeocoderException(exception: Exception) {
        Log.e("MapsActivity", "Geocoder service error", exception)
        locationTextView.text = "Geocoder service error"
        showToast("Geocoder service error")
    }

    private fun updateLocationInFragment(address: String) {
        // Replace `YourType` with the actual type you use with OfferFragment
        val fragment = supportFragmentManager.findFragmentByTag("OfferFragment") as? OfferFragment<*>
        fragment?.updateLocation(address)
    }


    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
        private const val REQUEST_SAVE_LOCATION = 2
    }
}
