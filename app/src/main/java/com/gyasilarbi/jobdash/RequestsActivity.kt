package com.gyasilarbi.jobdash

import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.gyasilarbi.jobdash.adapter.Tab2Adapter
import com.gyasilarbi.jobdash.databinding.ActivityRequestsBinding // Updated to match your layout file name

class RequestsActivity : AppCompatActivity() {

    // Use the correct type for view binding
    private lateinit var binding: ActivityRequestsBinding
    private lateinit var adapter: Tab2Adapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Initialize view binding
        binding = ActivityRequestsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Handle the back button click
        val backButton: ImageView = binding.backButtonImage
        backButton.setOnClickListener {
            onBackPressed()
        }

        adapter = Tab2Adapter(supportFragmentManager, lifecycle)
        binding.viewPager2.adapter = adapter
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab != null) {
                    binding.viewPager2.currentItem = tab.position
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }
            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })
        binding.viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {

            override fun onPageSelected(position: Int) {
                binding.tabLayout.selectTab(binding.tabLayout.getTabAt(position))
            }
        })
    }
}
