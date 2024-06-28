package com.gyasilarbi.jobdash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.gyasilarbi.jobdash.databinding.ActivityRegisterBinding
import io.getstream.chat.android.models.User
import java.util.*

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var mDbRef: DatabaseReference

    @SuppressLint("DefaultLocale")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase Authentication
        firebaseAuth = FirebaseAuth.getInstance()

        binding.signInButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        // Initialize views
        val cityEt: Spinner = binding.cityEt

        // Create an array of cities
        val cities = arrayOf("Accra", "Kumasi", "Takoradi", "Cape Coast", "Sunyani")

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, cities)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        cityEt.adapter = adapter

        // Optional: Set a listener to handle item selections
        cityEt.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedCity = cities[position]
                // Do something with the selected city
                Toast.makeText(this@RegisterActivity, "Selected city: $selectedCity", Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
        }

        // Set OnClickListener to Sign Up button
        binding.signUpButton.setOnClickListener {
            val email = binding.edtEmail.editText?.text.toString().trim()
            val password = binding.edtPassword.editText?.text.toString().trim()
            val confirmPass = binding.edtConfirmPassword.editText?.text.toString().trim()
            val firstName = binding.firstName.editText?.text.toString().trim()
            val lastName = binding.lastName.editText?.text.toString().trim()
            val city = cityEt.selectedItem.toString()

            // Validate fields
            if (email.isEmpty() || password.isEmpty() || confirmPass.isEmpty() || firstName.isEmpty() || lastName.isEmpty() || city.isEmpty()) {
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (city !in cities) {
                Toast.makeText(this, "Please select a valid city", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password != confirmPass) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Add password length constraint
            if (password.length < 8 || password.length > 20) {
                Toast.makeText(this, "Password must be between 8 and 20 characters", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Create user with email and password using Firebase Authentication
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Registration successful, navigate to next screen
                        Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show()

                        addUserToDatabase(firstName, lastName, email, firebaseAuth.currentUser?.uid!!)
                        val intent = Intent(this, LoginActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        // Registration failed
                        Toast.makeText(this, "Registration failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    private fun addUserToDatabase(firstName: String, lastName: String, email: String, uid: String?) {
        mDbRef = FirebaseDatabase.getInstance().getReference()

        mDbRef.child("user").child(uid!!).setValue(User(firstName, lastName, email, uid))
    }
}

