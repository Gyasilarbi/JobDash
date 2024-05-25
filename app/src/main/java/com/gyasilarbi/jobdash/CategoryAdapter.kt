package com.gyasilarbi.jobdash

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gyasilarbi.jobdash.databinding.ItemCategoryBinding

class CategoryAdapter(
    private val categories: List<Category>,
    private val onItemClicked: ((category: Category) -> Unit)
) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder =
        CategoryViewHolder(
            ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val currentCategory = categories[position]
        holder.bindData(currentCategory)
    }

    override fun getItemCount() = categories.size

    inner class CategoryViewHolder(private val binding: ItemCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindData(category: Category) {
            binding.categoryName.text = category.name
            binding.categoryOwner.text = category.owner
            binding.categoryLogo.setImageResource(category.logoResourceId)
            binding.starText.text = category.description
            binding.categoryPrice.text = category.price

            binding.root.setOnClickListener {
                onItemClicked(category)
            }
        }
    }
}
