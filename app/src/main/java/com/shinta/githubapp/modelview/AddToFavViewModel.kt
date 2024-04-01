package com.shinta.githubapp.modelview

import android.app.Application
import androidx.lifecycle.ViewModel
import com.shinta.githubapp.data.database.FavoriteUser
import com.shinta.githubapp.data.repository.FavUserRepository

class AddtoFavVM (application: Application) : ViewModel(){
    private val mListFavRepo: FavUserRepository = FavUserRepository(application)

    fun insert(favUser: FavoriteUser) {
        mListFavRepo.insert(favUser)
    }

//    fun update(favUser: Favorite) {
//        mListFavRepo.update(favUser)
//    }

    fun delete(favUser: FavoriteUser) {
        mListFavRepo.delete(favUser)
    }


}