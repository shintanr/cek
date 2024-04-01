package com.shinta.githubapp.data.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.shinta.githubapp.data.database.FavoriteUser
import com.shinta.githubapp.data.database.FavoriteUserDao
import com.shinta.githubapp.data.database.FavoriteUserRoomDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FavUserRepository(application: Application) {


    private val mFavoriteUserDao: FavoriteUserDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = FavoriteUserRoomDatabase.getDatabase(application)
        mFavoriteUserDao = db.favoriteUserDao()
    }
    fun getAllFavorite(): LiveData<List<FavoriteUser>> = mFavoriteUserDao.getAllFavorite()

    fun insert(favorite: FavoriteUser) {
        executorService.execute { mFavoriteUserDao.insert(favorite) }

    }
    fun delete(favorite: FavoriteUser) {
        executorService.execute { mFavoriteUserDao.delete(favorite) }
    }

    fun getFavoriteUserByUsername(uname: String): LiveData<FavoriteUser> = mFavoriteUserDao.getFavoriteUserByUsername(uname)
}