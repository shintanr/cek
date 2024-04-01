package com.shinta.githubapp.modelview

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shinta.githubapp.data.response.GithubResponse
import com.shinta.githubapp.data.response.Users
import com.shinta.githubapp.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {
    private val _githubResponse = MutableLiveData<GithubResponse>()
    val users: LiveData<GithubResponse> = _githubResponse

    private val _listUser = MutableLiveData<List<Users>>()
    val listUser: LiveData<List<Users>>  = _listUser

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading


    init {
        findUsers()
    }

    private fun findUsers() {
        _isLoading.value = true

        val client = ApiConfig.getApiService().getUsers(USERNAME)

        client.enqueue(object: Callback<GithubResponse>{
            override fun onResponse(
                call:Call<GithubResponse>,
                response: Response<GithubResponse>
            ) {
                _isLoading.value = false
                if(response.isSuccessful){
                    val responseBody = response.body()
                    if(responseBody != null ) {
                        _listUser.value = response.body()?.items as List<Users>
                    } else {
                        Log.e(TAG, "Failure: ${response.message()}")
                    }
                }

                }
            override fun onFailure(call: Call<GithubResponse>, t: Throwable) {
                _isLoading.value = true
                Log.e(TAG, "failure: ${t.message}")
            }
        })
    }

    fun findUsersBySearch(userSearch: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUsers(userSearch)

        client.enqueue(object: Callback<GithubResponse> {
            override fun onResponse(
                call: Call<GithubResponse>,
                response: Response<GithubResponse>
            ) {
                _isLoading.value = false
                if(response.isSuccessful) {
                    val responseBody = response.body()
                    if(responseBody != null) {
                        _listUser.value = response.body()?.items as List<Users>?
                    } else {
                        Log.e(TAG, "failure: ${response.message()}")
                    }
                }

            }

            override fun onFailure(call: Call<GithubResponse>, t: Throwable) {
                _isLoading.value = true
                Log.e(TAG, "failure: ${t.message}")
            }
        })
    }

    companion object {
        private const val TAG = "MainViewModel"
        private const val USERNAME = "shinta"
    }
}
