package com.gyasilarbi.jobdash

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ChatActivity : AppCompatActivity() {

    private lateinit var userRecyclerView: RecyclerView
    private lateinit var adapter: UserAdapter
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var mDbRef: DatabaseReference
    private lateinit var currentUserUid: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContentView(R.layout.activity_chat)

        // Initialize Firebase authentication
        firebaseAuth = FirebaseAuth.getInstance()
        val currentUser = firebaseAuth.currentUser
        if (currentUser != null) {
            currentUserUid = currentUser.uid // Get current user's UID
        } else {
            // Handle case when user is not authenticated
            finish() // Or redirect to login
            return
        }

        // Initialize Firebase database reference
        mDbRef = FirebaseDatabase.getInstance().reference.child("users")

        // Initialize RecyclerView and adapter
        userRecyclerView = findViewById(R.id.recycler_view)
        userRecyclerView.layoutManager = LinearLayoutManager(this)
        adapter = UserAdapter(emptyList()) { user ->
            // Handle item click if needed, e.g., open chat with user
        }
        userRecyclerView.adapter = adapter

        // Listen for changes in the database
        mDbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val users = mutableListOf<User>()
                for (postSnapshot in snapshot.children) {
                    val user = postSnapshot.getValue(User::class.java)
                    user?.let {
                        users.add(it)
                    }
                }
                adapter.updateUsers(users)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle database error
                // Log.e("ChatActivity", "Database error: ${error.message}")
            }
        })
    }
}
