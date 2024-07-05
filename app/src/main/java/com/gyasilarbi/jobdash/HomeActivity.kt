package com.gyasilarbi.jobdash

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
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

        val home = findViewById<LinearLayout>(R.id.home)
        home.setOnClickListener {
            val go = Intent (this, HomeActivity::class.java)
            startActivity(go)
        }

        val requests = findViewById<LinearLayout>(R.id.requests)
        requests.setOnClickListener {
            val go = Intent (this, RequestsActivity::class.java)
            startActivity(go)
        }

        val chats = findViewById<LinearLayout>(R.id.chat)
        chats.setOnClickListener {
            val go = Intent (this, ChatActivity::class.java)
            startActivity(go)
        }

        val account = findViewById<LinearLayout>(R.id.account)
        account.setOnClickListener {
            val go = Intent (this, Profile::class.java)
            startActivity(go)
        }
    }
}