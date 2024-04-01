package com.shinta.githubapp.data.response

import com.google.gson.annotations.SerializedName

data class GithubResponse(

	@field:SerializedName("total_count")
	val totalCount: Int,

	@field:SerializedName("items")
	val items: List<Users>
) {
	fun isNotEmpty(): Boolean {
		return items.isNotEmpty()
	}
}

data class Users(

	@field:SerializedName("following")
	val followingUrl: String,

	@field:SerializedName("login")
	val login: String,

	@field:SerializedName("followers")
	val followersUrl: String,

	@field:SerializedName("avatar_url")
	val avatarUrl: String,

	@field:SerializedName("id")
	val id: Int,

)
