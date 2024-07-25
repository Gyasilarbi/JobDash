package com.gyasilarbi.jobdash

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.gyasilarbi.jobdash.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var categoryAdapter: CategoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        categoryAdapter = CategoryAdapter(getCategory) { category ->
            val intent = Intent(this, CategoryDetailActivity::class.java).apply {
                putExtra("category", category)
            }
            startActivity(intent)
        }

        binding.categoryRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.categoryRecyclerView.adapter = categoryAdapter

        setupNavigation()
    }

    private fun setupNavigation() {
        binding.home.setOnClickListener {
            navigateTo(HomeActivity::class.java)
        }

        binding.requests.setOnClickListener {
            navigateTo(RequestsActivity::class.java)
        }

        binding.chat.setOnClickListener {
            navigateTo(ChatActivity::class.java)
        }

        binding.account.setOnClickListener {
            navigateTo(Profile::class.java)
        }

        binding.add.setOnClickListener {
            navigateTo(AddActivity::class.java)
        }
    }

    private fun navigateTo(activityClass: Class<*>) {
        val intent = Intent(this, activityClass)
        startActivity(intent)
    }
}
