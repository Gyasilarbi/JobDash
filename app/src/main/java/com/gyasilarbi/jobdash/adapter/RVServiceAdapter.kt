package com.gyasilarbi.jobdash.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gyasilarbi.jobdash.R
import com.gyasilarbi.jobdash.databinding.RvServiceItemBinding
import com.gyasilarbi.jobdash.models.Services

class RVServiceAdapter(
    private val services: List<Services>,
    private val onItemClicked: (Services) -> Unit
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

    inner class ServiceViewHolder(val binding: RvServiceItemBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClicked(services[position])
                }
            }
        }

        fun bind(service: Services) {
            binding.apply {
                title.text = service.title
                servicePrice.text = service.amount
                Glide.with(binding.root.context)
                    .load(service.imageUri) // Make sure this is a valid URI or URL
                    .placeholder(R.drawable.barber) // Optional placeholder
                    .error(R.drawable.barber) // Optional error image
                    .into(imageUri)
                description.text = service.description
                statusAns.text = if (service.status == 1) "Active" else "Inactive"
            }
        }

    }
}
