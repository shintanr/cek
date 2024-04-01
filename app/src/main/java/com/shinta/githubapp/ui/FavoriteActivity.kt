package com.shinta.githubapp.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.shinta.githubapp.R
import com.shinta.githubapp.adapter.FavoriteAdapter
import com.shinta.githubapp.databinding.ActivityFavoriteBinding
import androidx.recyclerview.widget.LinearLayoutManager
import com.shinta.githubapp.data.database.FavoriteUser
import com.shinta.githubapp.modelview.FavUserViewModel

class FavoriteActivity : AppCompatActivity() , FavoriteAdapter.OnItemClickCallback{

    private lateinit var binding: ActivityFavoriteBinding
    private lateinit var favoriteAdapter: FavoriteAdapter
    private val favUserViewModel by viewModels <FavUserViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        favoriteAdapter = FavoriteAdapter()
        binding.rvlistFav.layoutManager = LinearLayoutManager(this)
        binding.rvlistFav.adapter = favoriteAdapter
        binding.rvlistFav.setHasFixedSize(true)


        favoriteAdapter.setOnItemCLickCallback(this)

        favUserViewModel.getAllFavorite().observe(this) { users ->
            val items = arrayListOf<FavoriteUser>()
            users.map {
                val user = FavoriteUser(username = it.username, avatarUrl = it.avatarUrl)
                items.add(user)
            }
            favoriteAdapter.submitList(items)
        }
    }

    override fun onItemClicked(fav: FavoriteUser, position: Int) {
        val intent = Intent(this, DetailUserActivity::class.java)
        intent.putExtra("username", fav.username)
        startActivity(intent)
    }

}