package com.shinta.githubapp.ui

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.shinta.githubapp.R
import com.shinta.githubapp.adapter.UserAdapter
import com.shinta.githubapp.data.response.Users
import com.shinta.githubapp.databinding.ActivityMainBinding
import com.shinta.githubapp.helper.SettingModeModelFactory
import com.shinta.githubapp.helper.SettingPreference
import com.shinta.githubapp.helper.dataStore
import com.shinta.githubapp.modelview.MainViewModel
import com.shinta.githubapp.modelview.SettingModeViewModel
import com.shinta.githubapp.ui.FavoriteActivity
import com.shinta.githubapp.ui.SettingModeActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var settingModeViewModel: SettingModeViewModel
    private lateinit var adapter: UserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Hide the action bar
        supportActionBar?.hide()

        // Initialize ViewModel instances
        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        settingModeViewModel = ViewModelProvider(this, SettingModeModelFactory(
            SettingPreference.getInstance(application.dataStore)
        ))[SettingModeViewModel::class.java]

        // Initialize RecyclerView adapter
        adapter = UserAdapter()
        binding.rvUser.adapter = adapter

        // Set up RecyclerView layout manager and item decoration
        binding.rvUser.layoutManager = LinearLayoutManager(this)
        binding.rvUser.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))

        // Observe LiveData from ViewModel and update UI accordingly
        mainViewModel.users.observe(this) { users ->
            setUserData(users.items)
        }

        mainViewModel.listUser.observe(this) { userData ->
            setUserData(userData)
        }

        mainViewModel.isLoading.observe(this) { isLoading ->
            showLoading(isLoading)
        }

        // Observe dark mode setting changes
        settingModeViewModel.getThemeSettings().observe(this) { isDarkModeActive ->
            updateDarkMode(isDarkModeActive)
        }

        // Set up search functionality
        setupSearchFunctionality()

        // Set up click listeners for menu items
        binding.searchBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
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

    private fun setupSearchFunctionality() {
        binding.searchView.setupWithSearchBar(binding.searchBar)
        binding.searchView.editText.setOnEditorActionListener { textView, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val searchText = binding.searchView.text.toString()
                if (searchText.isNotEmpty()) {
                    binding.searchBar.setText(searchText)
                    binding.searchView.hide()
                    CoroutineScope(Dispatchers.Main).launch {
                        mainViewModel.findUsersBySearch(searchText)
                    }
                    true
                } else {
                    showToast("Please enter a search query")
                    false
                }
            }
            false
        }


        // Set up listener for search button in keyboard
//        binding.searchView.editText.setOnKeyListener { _, keyCode, event ->
//            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
//                val searchText = binding.searchView.text.toString()
//                if (searchText.isNotEmpty()) {
//                    CoroutineScope(Dispatchers.Main).launch {
//                        mainViewModel.findUsersBySearch(searchText)
//                    }
//                    true
//                } else {
//                    showToast("Please enter a search query")
//                    false
//                }
//            }
//            false
//        }
    }

    private fun setUserData(users: List<Users?>?) {
        adapter.submitList(users)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun updateDarkMode(isDarkModeActive: Boolean) {
        val mode = if (isDarkModeActive) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
        AppCompatDelegate.setDefaultNightMode(mode)
        updateMenuIconTint(isDarkModeActive)
    }

    private fun updateMenuIconTint(isDarkModeActive: Boolean) {
        val tint = if (isDarkModeActive) R.color.white else R.color.black
        binding.searchBar.menu.findItem(R.id.menu_favorite)?.icon?.setTint(ContextCompat.getColor(this, tint))
        binding.searchBar.menu.findItem(R.id.menuMode)?.icon?.setTint(ContextCompat.getColor(this, tint))
    }

    private fun openFavoriteActivity() {
        startActivity(Intent(this, FavoriteActivity::class.java))
    }

    private fun openModeSettingActivity() {
        startActivity(Intent(this, SettingModeActivity::class.java))
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
