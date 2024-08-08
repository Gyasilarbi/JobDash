package com.gyasilarbi.jobdash

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.InputFilter
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.switchmaterial.SwitchMaterial
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ServerValue
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.android.material.textfield.TextInputEditText
import com.gyasilarbi.jobdash.databinding.FragmentOfferBinding
import java.io.ByteArrayOutputStream
import java.util.UUID

private const val CURRENCY_SYMBOL = "GH₵ "
private const val REQUEST_PICK_IMAGE = 1
private const val REQUEST_LOCATION = 2

class OfferFragment<T> : Fragment() {

    private lateinit var firebaseRef: DatabaseReference
    private lateinit var storageRef: StorageReference
    private var imageUri: Uri? = null
    private var location: LatLng? = null

    private var _binding: FragmentOfferBinding? = null
    private val binding get() = _binding!!
    private lateinit var imageView: ImageView
    private lateinit var locationTextView: TextView

    private var isCameraImage: Boolean = false
    private lateinit var serviceStatusSwitch: SwitchMaterial

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            val lat = it.getDouble("latitude", 0.0)
            val lng = it.getDouble("longitude", 0.0)
            location = LatLng(lat, lng)
        }

        firebaseRef = FirebaseDatabase.getInstance().getReference("Services")
        storageRef = FirebaseStorage.getInstance().reference
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOfferBinding.inflate(inflater, container, false)
        return binding.root
    }

    fun updateLocation(location: String) {
        locationTextView.text = location
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupCategorySpinner()
        setupAmountInput()

        imageView = binding.imageView
        locationTextView = binding.locationTextView

        // Initialize the SwitchMaterial
        serviceStatusSwitch = binding.serviceStatusSwitch

        binding.buttonFromGallery.setOnClickListener {
            if (isCameraImage) {
                Toast.makeText(requireContext(), "Image already taken with camera", Toast.LENGTH_SHORT).show()
            } else {
                openGallery()
            }
        }

        binding.LocationSettings.setOnClickListener {
            navigateToMapsActivity()
        }

        binding.addService.setOnClickListener {
            saveDataService()
        }

        // Update location display if present
        updateLocationUI(location)
    }

    private fun updateLocationUI(location: LatLng?) {
        location?.let {
            val locationText = "Lat: ${it.latitude}, Lng: ${it.longitude}"
            locationTextView.text = locationText
        } ?: run {
            locationTextView.text = "No location selected"
        }
    }

    private fun saveDataService() {
        val title = binding.inputTitle.text.toString()
        val category = binding.categoryEt.selectedItem.toString()
        val description = binding.inputDescription.text.toString()
        val amount = binding.inputAmount.text.toString()
        val locationText = locationTextView.text.toString()
        val duration = "${binding.inputTime.text.toString()} hours"
        val userId = FirebaseAuth.getInstance().currentUser?.uid

        if (userId == null) {
            Toast.makeText(requireContext(), "User not logged in", Toast.LENGTH_SHORT).show()
            return
        }

        if (imageUri == null) {
            Toast.makeText(requireContext(), "Please select an image", Toast.LENGTH_SHORT).show()
            return
        }

        val serviceId = UUID.randomUUID().toString()
        val serviceStatus = if (serviceStatusSwitch.isChecked) 1 else 0 // 1 for active, 0 for inactive
        Log.d("OfferFragment", "Saving service with status: $serviceStatus")

        uploadImageToStorage(imageUri!!, serviceId, title, category, description, amount, locationText, duration, userId, serviceStatus)
    }

    private fun uploadImageToStorage(uri: Uri, serviceId: String, title: String, category: String, description: String, amount: String, locationText: String, duration: String, userId: String, serviceStatus: Int) {
        val imageRef = storageRef.child("images/$serviceId.jpg")

        imageRef.putFile(uri)
            .addOnSuccessListener {
                imageRef.downloadUrl.addOnSuccessListener { downloadUrl ->
                    saveServiceToDatabase(serviceId, title, category, description, amount, locationText, duration, downloadUrl.toString(), userId, serviceStatus)
                }
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Failed to upload image", Toast.LENGTH_SHORT).show()
            }
    }

    private fun saveServiceToDatabase(serviceId: String, title: String, category: String, description: String, amount: String, location: String, duration: String, imageUrl: String, userId: String, serviceStatus: Int) {
        val service = mapOf(
            "serviceId" to serviceId,
            "title" to title,
            "category" to category,
            "description" to description,
            "amount" to amount,
            "location" to location,
            "duration" to duration,
            "imageUrl" to imageUrl,
            "userId" to userId,
            "status" to serviceStatus, // Save status
            "createdAt" to ServerValue.TIMESTAMP
        )

        firebaseRef.child(serviceId).setValue(service)
            .addOnSuccessListener {
                // Clear input fields
                binding.inputTitle.text?.clear()
                binding.inputDescription.text?.clear()
                binding.inputAmount.text?.clear()
                binding.inputTime.text?.clear()
                binding.categoryEt.setSelection(0) // Reset to default or initial category
                locationTextView.text = "No location selected" // Reset location display
                imageView.setImageDrawable(null) // Clear the image view
                serviceStatusSwitch.isChecked = false // Reset the switch

                Toast.makeText(requireContext(), "Service added successfully", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Failed to add service", Toast.LENGTH_SHORT).show()
            }
    }

    private fun setupCategorySpinner() {
        val categoryEt = binding.categoryEt
        val categories = arrayOf(
            "Caregivers & Helps", "Plumbing", "Carpenters", "Electricians", "House Cleaning", "Cars & Mechanics", "Construction & Masonry", "Deliveries & Shipping",
            "Events & Fashion", "Farming & Aquaculturing", "Gardening & Plants", "Food & Catering",
            "Graphic & Video", "Health & Beauty", "Home Design & Renovation", "Marketing & Advertising",
            "Music & Audio", "Phones and Laptops", "Websites & Apps", "Writing & Translation"
        )
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        categoryEt.adapter = adapter

        categoryEt.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedCategory = categories[position]
                Toast.makeText(requireContext(), "Selected category: $selectedCategory", Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, REQUEST_PICK_IMAGE)
    }

    private fun navigateToMapsActivity() {
        val intent = Intent(requireContext(), MapsActivity::class.java)
        startActivityForResult(intent, REQUEST_LOCATION)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_PICK_IMAGE -> {
                    if (isCameraImage) {
                        Toast.makeText(requireContext(), "Image already taken with camera", Toast.LENGTH_SHORT).show()
                    } else {
                        imageUri = data?.data
                        if (imageUri != null) {
                            imageView.setImageURI(imageUri)
                            // Optionally upload the selected image to storage if needed
                        } else {
                            Toast.makeText(requireContext(), "Failed to select image", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                REQUEST_LOCATION -> {
                    val latitude = data?.getDoubleExtra("latitude", 0.0)
                    val longitude = data?.getDoubleExtra("longitude", 0.0)
                    val address = data?.getStringExtra("address")
                    val location = LatLng(latitude ?: 0.0, longitude ?: 0.0)
                    Log.d("OfferFragment", "Location received: $location, Address: $address")
                    updateLocationUI(location, address)
                }
            }
        }
    }

    private fun updateLocationUI(location: LatLng?, address: String?) {
        location?.let {
            locationTextView.text = "Address: $address"
        } ?: run {
            locationTextView.text = "No location selected"
        }
    }

    private fun setupAmountInput() {
        val inputAmount: TextInputEditText = binding.inputAmount
        inputAmount.setText(CURRENCY_SYMBOL)

        val filter = InputFilter { source, start, end, dest, dstart, dend ->
            if (dstart < CURRENCY_SYMBOL.length) {
                return@InputFilter TextUtils.concat(dest.subSequence(dstart, dend), source.subSequence(start, end))
            }
            null
        }
        inputAmount.filters = arrayOf(filter)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance(latitude: Double, longitude: Double) =
            OfferFragment<Any>().apply {
                arguments = Bundle().apply {
                    putDouble("latitude", latitude)
                    putDouble("longitude", longitude)
                }
            }
    }
}
