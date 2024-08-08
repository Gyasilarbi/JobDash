package com.gyasilarbi.jobdash

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.database.*
import com.gyasilarbi.jobdash.adapter.RVServiceAdapter
import com.gyasilarbi.jobdash.databinding.ActivityHomeBinding
import com.gyasilarbi.jobdash.models.Service
import java.util.Locale

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    private lateinit var serviceList: ArrayList<Service>
    private lateinit var firebaseRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupNavigation()

        firebaseRef = FirebaseDatabase.getInstance().getReference("Services")
        serviceList = arrayListOf()

        setupRecyclerView()
        fetchData()
        initializeLocationClient()
        checkLocationPermission()
    }

    private fun setupRecyclerView() {
        binding.serviceRecyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@HomeActivity)
        }
    }

    private fun initializeLocationClient() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
    }

    private fun checkLocationPermission() {
        requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                getLocation()
            } else {
                updateLocationText("Permission denied", "")
            }
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        } else {
            getLocation()
        }
    }

    private fun fetchData() {
        firebaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                serviceList.clear()
                if (snapshot.exists()) {
                    for (serviceSnapshot in snapshot.children) {
                        serviceSnapshot.getValue(Service::class.java)?.let { serviceList.add(it) }
                    }
                    updateRecyclerView()
                } else {
                    Toast.makeText(this@HomeActivity, "No services found", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@HomeActivity, "Failed to fetch data: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun updateRecyclerView() {
        val onItemClicked: (Service) -> Unit = { service ->
            val intent = Intent(this, ServiceDetailActivity::class.java).apply {
                putExtra("service", service) // Make sure 'service' contains a valid ID
            }
            startActivity(intent)

        }
        val rvAdapter = RVServiceAdapter(serviceList, onItemClicked)
        binding.serviceRecyclerView.adapter = rvAdapter
    }



    private fun getLocation() {
        try {
            fusedLocationClient.lastLocation
                .addOnSuccessListener(this, OnSuccessListener { location ->
                    location?.let {
                        val geocoder = Geocoder(this, Locale.getDefault())
                        val addresses = geocoder.getFromLocation(it.latitude, it.longitude, 1)
                        if (addresses != null) {
                            if (addresses.isNotEmpty()) {
                                val address = addresses[0]
                                val regionCountry = "${address.locality ?: "Unknown locality"}, ${address.countryName ?: "Unknown country"}"
                                val addressStreet = "${address.thoroughfare ?: "Unknown street"}, ${address.subThoroughfare ?: ""}".trim()
                                updateLocationText(regionCountry, addressStreet)
                            } else {
                                updateLocationText("Location not available", "")
                            }
                        }
                    } ?: run {
                        updateLocationText("Location not available", "")
                    }
                })
                .addOnFailureListener { e ->
                    updateLocationText("Failed to get location: ${e.message}", "")
                }
        } catch (e: SecurityException) {
            updateLocationText("SecurityException: ${e.message}", "")
        }
    }

    private fun updateLocationText(regionCountry: String, addressStreet: String) {
        binding.regionCountryTextView.text = regionCountry
        binding.addressStreetTextView.text = addressStreet
    }

    private fun setupNavigation() {
        binding.home.setOnClickListener { navigateTo(HomeActivity::class.java) }
        binding.idFABAdd.setOnClickListener { navigateTo(AddActivity::class.java) }
        binding.account.setOnClickListener { navigateTo(ProfileActivity::class.java) }
        binding.requests.setOnClickListener { navigateTo(RequestsActivity::class.java) }
    }

    private fun navigateTo(activityClass: Class<*>) {
        startActivity(Intent(this, activityClass))
    }
}
