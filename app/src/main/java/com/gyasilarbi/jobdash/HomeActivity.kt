package com.gyasilarbi.jobdash

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.gyasilarbi.jobdash.adapter.RVServiceAdapter
import com.gyasilarbi.jobdash.databinding.ActivityHomeBinding
import com.gyasilarbi.jobdash.models.Services
import java.util.Locale

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var regionCountryTextView: TextView
    private lateinit var addressStreetTextView: TextView
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>

    private lateinit var serviceList: ArrayList<Services>
    private lateinit var firebaseRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupNavigation()

        firebaseRef = FirebaseDatabase.getInstance().getReference("Services")
        serviceList = arrayListOf()

        fetchData()

        binding.serviceRecyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@HomeActivity)
        }

        // Initialize TextViews
        regionCountryTextView = findViewById(R.id.regionCountryTextView)
        addressStreetTextView = findViewById(R.id.addressStreetTextView)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                getLocation()
            } else {
                regionCountryTextView.text = "Permission denied"
                addressStreetTextView.text = ""
            }
        }

        // Explicitly check if permission is granted before requesting
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
                        val service = serviceSnapshot.getValue(Services::class.java)
                        service?.let { serviceList.add(it) }
                    }
                } else {
                    Toast.makeText(this@HomeActivity, "No services found", Toast.LENGTH_SHORT).show()
                }

                // Define the onItemClicked callback
                val onItemClicked: (Services) -> Unit = { service ->
                    // Handle item click here
                    Toast.makeText(this@HomeActivity, "Clicked on: ${service.serviceTitle}", Toast.LENGTH_SHORT).show()
                }

                // Pass the onItemClicked callback to the adapter
                val rvAdapter = RVServiceAdapter(serviceList, onItemClicked)
                binding.serviceRecyclerView.adapter = rvAdapter
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@HomeActivity, "Failed to fetch data: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }



    private fun getLocation() {
        try {
            fusedLocationClient.lastLocation
                .addOnSuccessListener(this, OnSuccessListener { location ->
                    if (location != null) {
                        val geocoder = Geocoder(this, Locale.getDefault())
                        val addresses: List<Address>? = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                        if (addresses != null && addresses.isNotEmpty()) {
                            val address = addresses[0]
                            // Display region and country
                            val regionCountry = "${address.locality ?: "Unknown locality"}, ${address.countryName ?: "Unknown country"}"
                            regionCountryTextView.text = regionCountry

                            // Display address and street name
                            val addressStreet = "${address.thoroughfare ?: "Unknown street"}, ${address.subThoroughfare ?: ""}".trim()
                            addressStreetTextView.text = addressStreet
                        } else {
                            regionCountryTextView.text = "Location not available"
                            addressStreetTextView.text = ""
                        }
                    } else {
                        regionCountryTextView.text = "Location not available"
                        addressStreetTextView.text = ""
                    }
                })
                .addOnFailureListener { e ->
                    regionCountryTextView.text = "Failed to get location: ${e.message}"
                    addressStreetTextView.text = ""
                }
        } catch (e: SecurityException) {
            regionCountryTextView.text = "SecurityException: ${e.message}"
            addressStreetTextView.text = ""
        }
    }

    private fun setupNavigation() {
        binding.home.setOnClickListener {
            navigateTo(HomeActivity::class.java)
        }

        binding.idFABAdd.setOnClickListener {
            navigateTo(AddActivity::class.java)
        }

        // HomeActivity.kt
        binding.account.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }



        binding.requests.setOnClickListener {
            navigateTo(RequestsActivity::class.java)
        }
    }

    private fun navigateTo(activityClass: Class<*>) {
        val intent = Intent(this, activityClass)
        startActivity(intent)
    }
}
