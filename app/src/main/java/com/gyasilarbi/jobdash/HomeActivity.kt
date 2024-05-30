package com.gyasilarbi.jobdash

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
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

        val clickHere = findViewById<TextView>(R.id.clickHere)
        clickHere.setOnClickListener {
            val go = Intent (this, ItemDetail::class.java)
            startActivity(go)
        }
    }


}