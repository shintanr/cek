package com.shinta.githubapp.ui

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.shinta.githubapp.R
import com.shinta.githubapp.adapter.SectionsPagerAdapter
import com.shinta.githubapp.data.database.FavoriteUser
import com.shinta.githubapp.data.response.DetailUserResponse
import com.shinta.githubapp.databinding.ActivityDetailUserBinding
import com.shinta.githubapp.helper.ViewModelFactory
import com.shinta.githubapp.modelview.DetailUserViewModel
import com.shinta.githubapp.modelview.FavAddUpdateViewModel
import com.shinta.githubapp.modelview.FavUserViewModel

class DetailUserActivity : AppCompatActivity() {
    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )
    }

    private lateinit var binding: ActivityDetailUserBinding
    private lateinit var favUserViewModel: FavUserViewModel
    private lateinit var favAddUpdateViewModel: FavAddUpdateViewModel

    val detailUserViewModel: DetailUserViewModel by viewModels()

    private var isFavorite = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        supportActionBar?.hide()
        val viewModelFactory = ViewModelFactory.getInstance(application)
        val detailUserViewModel: DetailUserViewModel by viewModels { viewModelFactory }
        val favUserViewModel: FavUserViewModel by viewModels { viewModelFactory }
        val favAddUpdateViewModel: FavAddUpdateViewModel by viewModels { viewModelFactory }

        val uname = intent.getStringExtra("username")
        if (uname != null) {
            detailUserViewModel.username = uname
        }

        detailUserViewModel.detailUser.observe(this, Observer { data ->
            setDataDetailUser(data)
        })
        favUserViewModel.getFavorite(uname ?: "").observe(this, Observer { favoriteUser ->
            isFavorite = favoriteUser != null
            updateFavoriteButtonUI(isFavorite)
        })


//        val detailUserViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[DetailUserViewModel::class.java]

        detailUserViewModel.username= intent.extras?.getString("username").toString()
        detailUserViewModel.detailUser.observe(this){userDetail ->
            setDataDetailUser(userDetail)
        }
        detailUserViewModel.isLoading.observe(this){
            showLoading(it)
        }

        binding.favBtn.setOnClickListener {
            if (isFavorite) {
                favAddUpdateViewModel.delete(FavoriteUser(uname ?: ""))
            } else {
                detailUserViewModel.detailUser.value?.let {
                    val favoriteUser = FavoriteUser(uname ?: "", it.avatarUrl)
                    favAddUpdateViewModel.insert(favoriteUser)
                }
            }
        }

        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        val viewPager: ViewPager2 = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
        supportActionBar?.elevation = 0f



    }

    private fun updateFavoriteButtonUI(favorite: Boolean) {
        if (isFavorite) {
            binding.favBtn.setImageResource(R.drawable.iv_favorite)
        } else {
            binding.favBtn.setImageResource(R.drawable.iv_favorite_border)
        }
    }


    private fun setDataDetailUser(detailUser: DetailUserResponse){

        binding.tvUsernameDetail.text = detailUser.login
        binding.tvUsernameAlias.text = detailUser.name
        binding.tvFollowers.text = "${detailUser.followers} Followers"
        binding.tvFollowing.text = "${detailUser.following} Following"

        Glide.with(this).load(detailUser.avatarUrl).into(binding.avatarDetail)
    }

    private fun showLoading(state: Boolean) {
        binding.progressBar.visibility = if (state) View.VISIBLE else View.GONE
    }
}