package com.gyasilarbi.jobdash

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton

class testActivity : AppCompatActivity() {

    private lateinit var addFAB: FloatingActionButton
    private lateinit var homeFAB: FloatingActionButton
    private lateinit var settingsFAB: FloatingActionButton
    private var fabVisible: Boolean = false

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_test)

        // Initialize FloatingActionButtons
        addFAB = findViewById(R.id.idFABAdd)
        homeFAB = findViewById(R.id.idFABHome)
        settingsFAB = findViewById(R.id.idFABSettings)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.root_layout)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Add click listener for Add FAB
        addFAB.setOnClickListener {
            if (!fabVisible) {
                homeFAB.show()
                settingsFAB.show()

                // Update FAB visibility
                homeFAB.visibility = View.VISIBLE
                settingsFAB.visibility = View.VISIBLE

                // Change Add FAB icon to close
                addFAB.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_close))

                fabVisible = true
            } else {
                homeFAB.hide()
                settingsFAB.hide()

                // Update FAB visibility
                homeFAB.visibility = View.GONE
                settingsFAB.visibility = View.GONE

                // Change Add FAB icon to add
                addFAB.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.add))

                fabVisible = false
            }
        }

        // Add click listener for Home FAB
        homeFAB.setOnClickListener {
            Toast.makeText(this, "Home clicked..", Toast.LENGTH_LONG).show()
        }

        // Add click listener for Settings FAB
        settingsFAB.setOnClickListener {
            Toast.makeText(this, "Settings clicked..", Toast.LENGTH_LONG).show()
        }
    }
}
