package com.gyasilarbi.jobdash

import android.content.Intent
import android.os.Bundle
import android.widget.ExpandableListView
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.gyasilarbi.jobdash.databinding.ActivityProfileBinding

class Profile : AppCompatActivity() {

    private lateinit var expandableListView: ExpandableListView
    private lateinit var listAdapter: MyExpandableListAdapter
    private lateinit var listDataHeader: MutableList<String>
    private lateinit var listDataChild: HashMap<String, List<String>>

    private lateinit var binding: ActivityProfileBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize view binding
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase Authentication
        firebaseAuth = FirebaseAuth.getInstance()

        // Handle the logout button click
        binding.btnLogout.setOnClickListener {
            firebaseAuth.signOut()
            clearLocalSessionData() // Clear local session data if needed

            // Navigate back to login screen
            val intent = Intent(this, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }

        // Initialize and set up the expandable list view
        expandableListView = binding.expandableListView
        prepareListData()
        listAdapter = MyExpandableListAdapter(this, listDataHeader, listDataChild)
        expandableListView.setAdapter(listAdapter)

        // Handle the back button click
        val backButton = binding.backButtonImage
        backButton.setOnClickListener {
            onBackPressed()
        }

        // Handle the home button click
        val home = binding.home
        home.setOnClickListener {
            val go = Intent(this, HomeActivity::class.java)
            startActivity(go)
        }

        // Handle the account button click
        val account = binding.account
        account.setOnClickListener {
            val go = Intent(this, Profile::class.java)
            startActivity(go)
        }
    }

    private fun clearLocalSessionData() {
        // Clear shared preferences or any other local session data if needed
        val sharedPref = getSharedPreferences("LoginPrefs", MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.clear()
        editor.apply()
    }

    private fun prepareListData() {
        listDataHeader = mutableListOf("Services", "Our Policies", "Reports / Feedback")
        listDataChild = HashMap()

        val group1 = listOf("Service 1", "Service 2", "Service 3")
        val group2 = listOf(
            "User Responsibilities: Outline the expected behavior of users, including accurate profile information and respectful communication.\n" +
                    "Prohibited Activities: List activities that are not allowed, such as spamming, fraud, and posting inappropriate content.\n" +
                    "Account Termination: Explain the conditions under which an account may be suspended or terminated."
        )
        val group3 = listOf(
            "Flagging System: Allow users to flag inappropriate or suspicious content (e.g., job listings, messages, user profiles).\n" +
                    "Immediate Actions: Provide immediate confirmation that a report has been received and outline the steps that will follow.\n" +
                    "Report Categories: Offer specific categories for reports such as:\n" +
                    "Fraudulent job postings\n" +
                    "Discrimination or harassment\n" +
                    "Inaccurate job descriptions\n" +
                    "Spam or scams\n" +
                    "Profile issues"
        )

        listDataChild[listDataHeader[0]] = group1
        listDataChild[listDataHeader[1]] = group2
        listDataChild[listDataHeader[2]] = group3
    }
}
