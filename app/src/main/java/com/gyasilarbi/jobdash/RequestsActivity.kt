package com.gyasilarbi.jobdash

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class RequestsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContentView(R.layout.activity_requests)

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