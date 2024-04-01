package com.shinta.githubapp.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.shinta.githubapp.data.response.Users
import com.shinta.githubapp.ui.DetailUserActivity
import com.shinta.githubapp.R
import com.shinta.githubapp.databinding.ItemUserBinding

class UserAdapter: ListAdapter<Users, UserAdapter.MyViewHolder>(DIFF_CALLBACK) {
    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Users>() {
            override fun areItemsTheSame(oldItem: Users, newItem: Users): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: Users, newItem: Users): Boolean {
                return oldItem == newItem
            }
        }
    }

    class MyViewHolder(val binding: ItemUserBinding): RecyclerView.ViewHolder(binding.root) {
        val avatar: ImageView = itemView.findViewById(R.id.avatar)
        fun bind(user: Users){
            binding.tvUsername.text = "${user.login}"
        }
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val user = getItem(position)
        holder.bind(user)
        Glide.with(holder.avatar.getContext()).load(user.avatarUrl).into(holder.avatar)

        holder.itemView.setOnClickListener{
            val intentDetailUser =  Intent(holder.itemView.context, DetailUserActivity::class.java)
            intentDetailUser.putExtra("username",user.login)
            holder.itemView.context.startActivity(intentDetailUser)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }
}