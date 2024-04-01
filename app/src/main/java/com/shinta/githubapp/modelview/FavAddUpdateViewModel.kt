package com.shinta.githubapp.modelview

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.shinta.githubapp.data.database.FavoriteUser
import com.shinta.githubapp.data.repository.FavUserRepository

class FavAddUpdateViewModel (application: Application) : AndroidViewModel(application) {
    private val mFavoriteRepository: FavUserRepository = FavUserRepository(application)
    fun insert(favorite: FavoriteUser) {
        mFavoriteRepository.insert(favorite)
    }
    fun delete(favorite: FavoriteUser) {
        mFavoriteRepository.delete(favorite)
    }
}