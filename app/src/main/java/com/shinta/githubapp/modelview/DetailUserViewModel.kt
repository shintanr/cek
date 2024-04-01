package com.shinta.githubapp.modelview

import android.app.Application
import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shinta.githubapp.data.database.FavoriteUser
import com.shinta.githubapp.data.repository.FavUserRepository
import com.shinta.githubapp.data.response.DetailUserResponse
import com.shinta.githubapp.data.response.FollowingFollowersResponseItem
import com.shinta.githubapp.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailUserViewModel(application: Application) : AndroidViewModel(application) {

    private val _detailUser = MutableLiveData<DetailUserResponse>()
    val detailUser: LiveData<DetailUserResponse> = _detailUser

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _followingResponse = MutableLiveData<List<FollowingFollowersResponseItem>>()
    val followingResponse: LiveData<List<FollowingFollowersResponseItem>> = _followingResponse

    private val _followersResponse = MutableLiveData<List<FollowingFollowersResponseItem>>()
    val followersResponse: LiveData<List<FollowingFollowersResponseItem>> = _followersResponse

    private val mFavoriteRepository: FavUserRepository = FavUserRepository(application)

    var username: String = ""
        set(value) {
            field = value
            detailUser()
        }

    private fun detailUser() {
        _isLoading.value = true

        val client = ApiConfig.getApiService().getDetailUser(username)

        client.enqueue(object: Callback<DetailUserResponse> {
            override fun onResponse(
                call: Call<DetailUserResponse>,
                response: Response<DetailUserResponse>
            ) {
                _isLoading.value = false
                if(response.isSuccessful){
                    val responseBody = response.body()
                    if(responseBody != null ) {
                        _detailUser.value = responseBody
                    } else {
                        Log.e(TAG, "Failure: ${response.message()}")
                    }
                }

            }
            override fun onFailure(call: Call<DetailUserResponse>, t: Throwable) {
                _isLoading.value = true
                Log.e(TAG, "failure: ${t.message}")
            }
        })
    }

    fun getFollowersUser(userx: String) {
        _isLoading.value = true

        val client = ApiConfig.getApiService().getFollowersUser(userx)

        client.enqueue(object: Callback<List<FollowingFollowersResponseItem>> {
            override fun onResponse(
                call: Call<List<FollowingFollowersResponseItem>>,
                response: Response<List<FollowingFollowersResponseItem>>
            ) {
                _isLoading.value = false
                if(response.isSuccessful){
                    val responseBody = response.body()
                    if(responseBody != null ) {
                        _followersResponse.value = responseBody
                    } else {
                        Log.e(TAG, "Failure: ${response.message()}")
                    }
                }

            }
            override fun onFailure(call: Call<List<FollowingFollowersResponseItem>>, t: Throwable) {
                _isLoading.value = true
                Log.e(TAG, "failure: ${t.message}")
            }
        })
    }

    fun getFollowingUser(userx: String) {
        _isLoading.value = true

        val client = ApiConfig.getApiService().getFollowingUser(userx)

        client.enqueue(object: Callback<List<FollowingFollowersResponseItem>> {
            override fun onResponse(
                call: Call<List<FollowingFollowersResponseItem>>,
                response: Response<List<FollowingFollowersResponseItem>>
            ) {
                _isLoading.value = false
                if(response.isSuccessful){
                    val responseBody = response.body()
                    if(responseBody != null ) {
                        _followingResponse.value = responseBody
                    } else {
                        Log.e(TAG, "Failure: ${response.message()}")
                    }
                }

            }
            override fun onFailure(call: Call<List<FollowingFollowersResponseItem>>, t: Throwable) {
                _isLoading.value = true
                Log.e(TAG, "failure: ${t.message}")
            }
        })
    }

    fun insert(favorite: FavoriteUser) {
        mFavoriteRepository.insert(favorite)
    }
    fun delete(favorite: FavoriteUser) {
        mFavoriteRepository.delete(favorite)
    }
    fun getFavoriteUserByUsername(username: String): LiveData<FavoriteUser> {
        return mFavoriteRepository.getFavoriteUserByUsername(username)
    }
}