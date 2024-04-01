package com.shinta.githubapp.data.retrofit

import com.shinta.githubapp.data.response.DetailUserResponse
import com.shinta.githubapp.data.response.FollowingFollowersResponseItem
import com.shinta.githubapp.data.response.GithubResponse
import retrofit2.Call
import retrofit2.http.*


interface ApiService {
   @GET("search/users")
   fun getUsers(
       @Query("q") login: String
   ): Call<GithubResponse>


    @GET("users/{username}")
    fun getDetailUser(
        @Path("username") username: String
    ): Call<DetailUserResponse>


    @GET("users/{username}/following")
    fun getFollowingUser(
        @Path("username") username: String
    ): Call<List<FollowingFollowersResponseItem>>


    @GET("users/{username}/followers")
    fun getFollowersUser(
        @Path("username") username: String
    ): Call<List<FollowingFollowersResponseItem>>
}

