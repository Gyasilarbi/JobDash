package com.gyasilarbi.jobdash

import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ItemDetail : AppCompatActivity() {

    private var isThumbsUp = false // Initialize isThumbsUp variable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_item_detail)

        // Handle the back button click
        val backButton = findViewById<ImageView>(R.id.back_button_image)
        backButton.setOnClickListener {
            onBackPressed()
        }

        // Find the RatingBar by its ID
        val ratingBar = findViewById<RatingBar>(R.id.ratingBar)

        // Set an onRatingBarChangeListener
        ratingBar.setOnRatingBarChangeListener { _, rating, fromUser ->
            if (fromUser) {
                Toast.makeText(this, "Rating: $rating", Toast.LENGTH_SHORT).show()
            }
        }

        // Find the Thumbs Up button by its ID
        val thumbsUpButton: Button = findViewById(R.id.thumbsUpButton)

        // Set click listener for Thumbs Up button
        thumbsUpButton.setOnClickListener {
            isThumbsUp = !isThumbsUp
            updateButtonState(thumbsUpButton)
        }
    }

    private fun enableEdgeToEdge() {
        // Enable edge-to-edge display
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content)) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            WindowInsetsCompat.CONSUMED
        }
    }

    private fun updateButtonState(button: Button) {
        if (isThumbsUp) {
            button.setBackgroundResource(R.drawable.baseline_thumb_up_24) // Change to the thumbs up on drawable
            button.text = "Liked" // Change text if necessary
            button.setTextColor(Color.GREEN) // Change text color if necessary
        } else {
            button.setBackgroundResource(R.drawable.baseline_thumb_up_off_alt_24) // Change to the thumbs up off drawable
            button.text = "Like" // Change text if necessary
            button.setTextColor(Color.BLACK) // Change text color if necessary
        }
    }
}
