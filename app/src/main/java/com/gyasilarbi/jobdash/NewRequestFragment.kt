package com.gyasilarbi.jobdash

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*
import com.gyasilarbi.jobdash.adapter.NewRequestAdapter
import com.gyasilarbi.jobdash.databinding.FragmentNewRequestBinding
import com.gyasilarbi.jobdash.models.Request

class NewRequestFragment<T> : Fragment() {

    private var _binding: FragmentNewRequestBinding? = null
    private val binding get() = _binding!!
    private var requestList : ArrayList<Request> = ArrayList()
    private lateinit var firebaseRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firebaseRef = FirebaseDatabase.getInstance().getReference("Requests")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewRequestBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        fetchData() // Fetch data after RecyclerView is set up
    }

    private fun setupRecyclerView() {
        binding.newRequestRecyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = NewRequestAdapter(requestList) // Initial empty adapter
        }
    }

    private fun fetchData() {
        firebaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                requestList.clear()
                if (snapshot.exists()) {
                    for (requestSnapshot in snapshot.children) {
                        requestSnapshot.getValue(Request::class.java)?.let { requestList.add(it) }
                    }
                    updateRecyclerView() // Notify adapter of data change
                } else {
                    Toast.makeText(requireContext(), "No requests found", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "Failed to fetch data: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun updateRecyclerView() {
        val requestAdapter = NewRequestAdapter(requestList)
        binding.newRequestRecyclerView.adapter = requestAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}