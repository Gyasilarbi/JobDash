package com.gyasilarbi.jobdash

import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.RatingBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.gyasilarbi.jobdash.databinding.ActivityCategoryDetailBinding

class CategoryDetailActivity : AppCompatActivity() {

    private var isThumbsUp = false // Initialize isThumbsUp variable


    private lateinit var binding: ActivityCategoryDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCategoryDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backButton.setOnClickListener{
            onBackPressed()
        }

        val category = intent.getParcelableExtra<Category>("category")
        category?.let {
            binding.categoryName.text = it.name
            binding.categoryOwner.text = it.owner
            binding.categoryLogo.setImageResource(it.logoResourceId)
            binding.categoryDescription.text = it.description
            binding.categoryPrice.text = it.price
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

