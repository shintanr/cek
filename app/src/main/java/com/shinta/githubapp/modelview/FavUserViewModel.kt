package com.shinta.githubapp.modelview

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.shinta.githubapp.data.database.FavoriteUser
import com.shinta.githubapp.data.repository.FavUserRepository

class FavUserViewModel (application: Application) : AndroidViewModel(application){
    private val repositoryFav : FavUserRepository = FavUserRepository(application)

    fun getAllFavorite(): LiveData<List<FavoriteUser>> {
        return repositoryFav.getAllFavorite()
    }
    fun getFavorite(uname: String): LiveData<FavoriteUser> = repositoryFav.getFavoriteUserByUsername(uname)
}