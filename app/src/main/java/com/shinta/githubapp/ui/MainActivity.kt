package com.shinta.githubapp.ui


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.search.SearchBar
import com.shinta.githubapp.R
import com.shinta.githubapp.adapter.UserAdapter
import com.shinta.githubapp.data.response.Users
import com.shinta.githubapp.databinding.ActivityMainBinding
import com.shinta.githubapp.helper.SettingModeModelFactory
import com.shinta.githubapp.helper.SettingPreference
import com.shinta.githubapp.helper.dataStore
import com.shinta.githubapp.modelview.MainViewModel
import com.shinta.githubapp.modelview.SettingModeViewModel


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.hide()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val mainViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(MainViewModel::class.java)
        mainViewModel.users.observe(this) { users ->
            setUserData(users.items)
        }

        mainViewModel.listUser.observe(this) { userData ->
            setUserData(userData)
        }

        mainViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        val pref = SettingPreference.getInstance(application.dataStore)
        val settingModeViewModel = ViewModelProvider(this, SettingModeModelFactory(pref)).get(
            SettingModeViewModel::class.java)

        val layoutManager = LinearLayoutManager(this)
        binding.rvUser.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvUser.addItemDecoration(itemDecoration)

        settingModeViewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
            updateMenuIconTint(isDarkModeActive)
        }
        with(binding) {
            searchView.setupWithSearchBar(searchBar)
            searchView
                .editText
                .setOnEditorActionListener { textView, actionId, event ->
                    val searchText = searchView.text.toString()
                    if (searchText.isNotEmpty()) {
                        searchBar.setText(searchText)
                        searchView.hide()
                        mainViewModel.findUsersBySearch(searchText)
                        false
                    } else {
                        Toast.makeText(
                            this@MainActivity,
                            "Please enter a search query",
                            Toast.LENGTH_SHORT
                        ).show()
                        true
                    }
                }
        }

        val searchBar = findViewById<SearchBar>(R.id.searchBar)
        searchBar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.menu_favorite -> {
                    openFavoriteActivity()
                    true
                }
                R.id.menuMode -> {
                    openModeSettingActivity()
                    true
                }
                else -> false
            }
        }
    }

    private fun openModeSettingActivity() {
        val intent = Intent(this, SettingModeActivity::class.java)
        startActivity(intent)
    }

    private fun openFavoriteActivity() {
        val intent = Intent(this, FavoriteActivity::class.java)
        startActivity(intent)
    }

    private fun setUserData(users: List<Users?>?) {

        val adapter = UserAdapter()
        adapter.submitList(users)
        binding.rvUser.adapter = adapter
    }

    private fun showLoading(state: Boolean) {
        binding.progressBar.visibility = if(state) View.VISIBLE else View.GONE
    }

    private fun updateMenuIconTint(isDarkModeActive: Boolean) {
        val menu = binding.searchBar.menu
        val favoriteMenuItem = menu.findItem(R.id.menu_favorite)
        val modeMenuItem = menu.findItem(R.id.menuMode)

        val tint = if (isDarkModeActive) {
            resources.getColor(R.color.white, theme)
        } else {
            resources.getColor(R.color.black, theme)
        }

        favoriteMenuItem.icon?.setTint(tint)
        modeMenuItem.icon?.setTint(tint)
    }

}