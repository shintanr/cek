package com.shinta.githubapp.data.response

import com.google.gson.annotations.SerializedName

data class FollowingFollowersResponse(

	@field:SerializedName("FollowingFollowersResponse")
	val followingFollowersResponse: List<FollowingFollowersResponseItem>
)

data class FollowingFollowersResponseItem(


	@field:SerializedName("following")
	val following: String,

	@field:SerializedName("login")
	val login: String,

	@field:SerializedName("followers")
	val followers: String,

	@field:SerializedName("type")
	val type: String,

	@field:SerializedName("avatar_url")
	val avatarUrl: String,

	@field:SerializedName("id")
	val id: Int,


)
