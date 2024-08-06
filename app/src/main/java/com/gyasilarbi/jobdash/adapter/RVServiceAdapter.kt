package com.gyasilarbi.jobdash.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gyasilarbi.jobdash.R
import com.gyasilarbi.jobdash.ServiceDetailActivity
import com.gyasilarbi.jobdash.databinding.RvServiceItemBinding
import com.gyasilarbi.jobdash.models.Service

class RVServiceAdapter(
    private val services: List<Service>,
    private val onItemClicked: (Service) -> Unit
) : RecyclerView.Adapter<RVServiceAdapter.ServiceViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceViewHolder =
        ServiceViewHolder(
            RvServiceItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun onBindViewHolder(holder: ServiceViewHolder, position: Int) {
        val currentService = services[position]
        holder.bind(currentService)
    }

    override fun getItemCount() = services.size

    inner class ServiceViewHolder(private val binding: RvServiceItemBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val service = services[position]
                    val context = binding.root.context
                    val intent = Intent(context, ServiceDetailActivity::class.java)
                    intent.putExtra("service", service)
                    context.startActivity(intent)
                }
            }
        }

        fun bind(service: Service) {
            binding.apply {
                title.text = service.title
                price.text = service.amount // Convert amount to String if needed
                description.text = service.description
                statusAns.text = if (service.status == 1) "Active" else "Inactive"

                // Load image with Glide
                val imageUrl = service.imageUrl
                if (imageUrl != null) {
                    if (imageUrl.isNotEmpty()) {
                        Glide.with(binding.root.context)
                            .load(imageUrl)
                            .placeholder(R.drawable.barber) // Optional placeholder
                            .error(R.drawable.barber) // Optional error image
                            .into(serviceImage)
                    } else {
                        // Fallback image if no URI provided
                        Glide.with(binding.root.context)
                            .load(R.drawable.barber)
                            .into(serviceImage)
                    }
                }
            }
        }
    }
}
