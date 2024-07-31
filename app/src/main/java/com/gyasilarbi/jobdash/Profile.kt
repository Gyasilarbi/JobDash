package com.gyasilarbi.jobdash

import android.content.Intent
import android.os.Bundle
import android.widget.ExpandableListView
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.gyasilarbi.jobdash.databinding.ActivityProfileBinding

class ProfileActivity : AppCompatActivity() {

    private lateinit var expandableListView: ExpandableListView
    private lateinit var listAdapter: MyExpandableListAdapter
    private lateinit var listDataHeader: MutableList<String>
    private lateinit var listDataChild: HashMap<String, List<String>>

    private lateinit var binding: ActivityProfileBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var mDbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase Authentication and DatabaseReference
        firebaseAuth = FirebaseAuth.getInstance()
        mDbRef = FirebaseDatabase.getInstance().getReference("user")

        // Load user data
        loadUserData()

        // Set OnClickListener for logout button
        binding.btnLogout.setOnClickListener {
            showLogoutConfirmationDialog()
        }

        // Handle the back button click
        val backButton: ImageView = binding.backButtonImage
        backButton.setOnClickListener {
            onBackPressed()
        }

        // Initialize and set up the expandable list view
        expandableListView = binding.expandableListView
        prepareListData()
        listAdapter = MyExpandableListAdapter(this, listDataHeader, listDataChild)
        expandableListView.setAdapter(listAdapter)
    }

    private fun loadUserData() {
        val uid = firebaseAuth.currentUser?.uid
        if (uid != null) {
            mDbRef.child(uid).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: com.google.firebase.database.DataSnapshot) {
                    val user = snapshot.getValue(User::class.java)
                    if (user != null) {
                        // Populate UI with user data
                        binding.username.text = user.name
                        binding.email.text = user.email
                        binding.phone.text = user.phone
                        binding.city.text = user.city
                    } else {
                        Toast.makeText(this@ProfileActivity, "User data not found", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onCancelled(error: com.google.firebase.database.DatabaseError) {
                    Toast.makeText(this@ProfileActivity, "Failed to load user data: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showLogoutConfirmationDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Logout")
        builder.setMessage("Are you sure you want to logout?")
        builder.setPositiveButton("Yes") { _, _ ->
            performLogout()
        }
        builder.setNegativeButton("No", null)
        builder.show()
    }

    private fun performLogout() {
        firebaseAuth.signOut()
        Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show()

        // Navigate to LoginActivity
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    data class User(
        val name: String = "",
        val email: String = "",
        val uid: String = "",
        val city: String = "",
        val phone: String = "",
        val banned: Boolean = false,
        val id: String = "",
        val image: String = "",
        val invisible: Boolean = false,
        val online: Boolean = false,
        val role: String = "",
        val totalUnreadCount: Int = 0,
        val unreadChannels: Int = 0
    )

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
