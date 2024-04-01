package com.shinta.githubapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.shinta.githubapp.R
import com.shinta.githubapp.data.database.FavoriteUser
import com.shinta.githubapp.databinding.ItemUserBinding

class FavoriteAdapter : ListAdapter<FavoriteUser, FavoriteAdapter.ViewHolder>(DIFF_CALLBACK) {

    private var onItemClickCallback: OnItemClickCallback? = null

    fun setOnItemCLickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    inner class ViewHolder(private val binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: FavoriteUser) {
            binding.tvUsername.text = item.username
            binding.apply {
                Glide.with(itemView.context)
                    .load(item.avatarUrl)
                    .centerCrop()
                    .error(R.drawable.ic_launcher_foreground)
                    .into(binding.avatar)
                tvUsername.text = item.username
            }
            binding.root.setOnClickListener {
                onItemClickCallback?.onItemClicked(item, adapterPosition)
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(fav: FavoriteUser, position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bind(item)
        } else {

        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<FavoriteUser>() {
            override fun areItemsTheSame(oldItem: FavoriteUser, newItem: FavoriteUser): Boolean {
                return oldItem.username == newItem.username
            }
            override fun areContentsTheSame(oldItem: FavoriteUser, newItem: FavoriteUser): Boolean {
                return oldItem == newItem
            }
        }
    }
}