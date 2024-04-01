package com.shinta.githubapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.shinta.githubapp.data.response.FollowingFollowersResponseItem
import com.shinta.githubapp.R
import com.shinta.githubapp.databinding.ItemFollowBinding


class FollowAdapter : ListAdapter<FollowingFollowersResponseItem, FollowAdapter.MyViewHolder>(DIFF_CALLBACK) {

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<FollowingFollowersResponseItem>() {
            override fun areItemsTheSame(oldItem: FollowingFollowersResponseItem, newItem: FollowingFollowersResponseItem): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: FollowingFollowersResponseItem, newItem: FollowingFollowersResponseItem): Boolean {
                return oldItem == newItem
            }
        }
    }

    class MyViewHolder(val binding: ItemFollowBinding): RecyclerView.ViewHolder(binding.root) {
        val avatarFollow: ImageView = itemView.findViewById(R.id.avatar_follow)
        fun bind(follow: FollowingFollowersResponseItem){
            binding.tvUsernameFollow.text = "${follow.login}"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemFollowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FollowAdapter.MyViewHolder, position: Int) {
        val follow = getItem(position)
        holder.bind(follow)
        Glide.with(holder.avatarFollow.getContext()).load(follow.avatarUrl).into(holder.binding.avatarFollow)
        }
}