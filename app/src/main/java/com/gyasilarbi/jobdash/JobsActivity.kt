package com.gyasilarbi.jobdash

import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.gyasilarbi.jobdash.databinding.ActivityJobsBinding

class JobsActivity : AppCompatActivity() {

    // Use the correct type for view binding
    private lateinit var binding: ActivityJobsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Initialize view binding
        binding = ActivityJobsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Handle the back button click
        val backButton: ImageView = binding.backButtonImage
        backButton.setOnClickListener {
            onBackPressed()
        }
    }
}