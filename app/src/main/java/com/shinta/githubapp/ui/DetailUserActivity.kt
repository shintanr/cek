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
import com.shinta.githubapp.modelview.AddToFavViewModel
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

    // Deklarasi viewmodel menggunakan viewModels() Kotlin property delegate
    private val addtoFavVM: AddToFavViewModel by viewModels()
    private lateinit var binding: ActivityDetailUserBinding
    private lateinit var detailUserViewModel: DetailUserViewModel
    private lateinit var favoriteAddDeleteViewModel: FavAddUpdateViewModel
    private lateinit var favoriteViewModel: FavUserViewModel

    private var isFavorite = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_user)

        // Sembunyikan ActionBar
        supportActionBar?.hide()

        // Inisialisasi ViewModel menggunakan ViewModelProvider dan ViewModelFactory
        val viewModelFactory = ViewModelFactory.getInstance(application)
        detailUserViewModel = ViewModelProvider(this, viewModelFactory)[DetailUserViewModel::class.java]
        favoriteAddDeleteViewModel =
            ViewModelProvider(this, viewModelFactory).get(FavAddUpdateViewModel::class.java)
        favoriteViewModel =
            ViewModelProvider(this, viewModelFactory).get(FavUserViewModel::class.java)

        // Inflate binding layout
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Ambil username dari intent, jika ada
        val username = intent.getStringExtra("username")
        if (username != null) {
            detailUserViewModel.username = username
        }

        // Observasi perubahan data user detail
        detailUserViewModel.detailUser.observe(this){ userDetail ->
            if (userDetail != null) {
                setDataDetailUser(userDetail)
            }
        }

        // Observasi status loading
        detailUserViewModel.isLoading.observe(this){
            showLoading(it)
        }

        // Observasi status favorite user
        favoriteViewModel.getFavorite(username ?: "").observe(this, Observer { favoriteUser ->
            isFavorite = favoriteUser != null
            updateFavoriteButtonUI(isFavorite)
        })

        // Tambahkan onClickListener untuk tombol favorit
        binding.favBtn.setOnClickListener {
            if (isFavorite) {
                favoriteAddDeleteViewModel.delete(FavoriteUser(username ?: ""))
            } else {
                detailUserViewModel.detailUser.value?.let {
                    val favoriteUser = FavoriteUser(username ?: "", it.avatarUrl)
                    favoriteAddDeleteViewModel.insert(favoriteUser)
                }
            }
        }

        // Inisialisasi ViewPager dan TabLayout
        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        val viewPager: ViewPager2 = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
        supportActionBar?.elevation = 0f
    }

    // Fungsi untuk mengatur data detail user
    private fun setDataDetailUser(detailUser: DetailUserResponse){
        binding.tvUsernameDetail.text = detailUser.login
        binding.tvUsernameAlias.text = detailUser.name
        binding.tvFollowers.text = "${detailUser.followers} Followers"
        binding.tvFollowing.text = "${detailUser.following} Following"

        Glide.with(this).load(detailUser.avatarUrl).into(binding.avatarDetail)
    }

    // Fungsi untuk menampilkan/hide loading indicator
    private fun showLoading(state: Boolean) {
        binding.progressBar.visibility = if (state) View.VISIBLE else View.GONE
    }

    // Fungsi untuk mengupdate UI tombol favorit
    private fun updateFavoriteButtonUI(isFavorite: Boolean) {
        if (isFavorite) {
            binding.favBtn.setImageResource(R.drawable.iv_favorite)
        } else {
            binding.favBtn.setImageResource(R.drawable.iv_favorite_border)
        }
    }
}
