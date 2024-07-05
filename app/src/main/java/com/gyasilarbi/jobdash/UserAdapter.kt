package com.gyasilarbi.jobdash

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gyasilarbi.jobdash.databinding.UserLayoutBinding

class UserAdapter(
    private var users: List<User>,
    private val onItemClicked: (user: User) -> Unit
) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = UserLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(users[position])
    }

    override fun getItemCount(): Int {
        return users.size
    }

    fun updateUsers(updatedUsers: List<User>) {
        users = updatedUsers
        notifyDataSetChanged()
    }

    inner class UserViewHolder(private val binding: UserLayoutBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(user: User) {
            binding.txtName.text = user.name

            binding.root.setOnClickListener {
                onItemClicked(user)
            }
        }
    }
}
