package com.gyasilarbi.jobdash

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.gyasilarbi.jobdash.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var categoryAdapter: CategoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        categoryAdapter = CategoryAdapter(getCategory, {})

        binding.categoryRecyclerView.adapter = categoryAdapter
    }
}