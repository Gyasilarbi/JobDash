package com.gyasilarbi.jobdash

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Account : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_account)

        val home = findViewById<LinearLayout>(R.id.home)
        home.setOnClickListener {
            val go = Intent (this, HomeActivity::class.java)
            startActivity(go)
        }

        val account = findViewById<LinearLayout>(R.id.account)
        account.setOnClickListener {
            val go = Intent (this, Account::class.java)
            startActivity(go)
        }
    }
}


