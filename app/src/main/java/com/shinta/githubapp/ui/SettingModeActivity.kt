package com.shinta.githubapp.ui

import android.os.Bundle
import android.widget.CompoundButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.switchmaterial.SwitchMaterial
import com.shinta.githubapp.R
import com.shinta.githubapp.helper.SettingModeModelFactory
import com.shinta.githubapp.helper.SettingPreference
import com.shinta.githubapp.helper.dataStore
import com.shinta.githubapp.modelview.SettingModeViewModel

class SettingModeActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting_mode)
        val switchTheme = findViewById<SwitchMaterial>(R.id.switch_theme)

        // Get preference instance for theme settings
        val pref = SettingPreference.getInstance(application.dataStore)

        // Initialize ViewModel for theme settings
        val modeSettingViewModel = ViewModelProvider(this, SettingModeModelFactory(pref)).get(
            SettingModeViewModel::class.java
        )

        // Observe theme settings changes and update UI accordingly
        modeSettingViewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                // Set dark mode if it's active
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                switchTheme.isChecked = true
            } else {
                // Set light mode if dark mode is not active
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                switchTheme.isChecked = false
            }
        }

        // Listen to switch changes and save theme setting accordingly
        switchTheme.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            modeSettingViewModel.saveThemeSetting(isChecked)
        }
    }
}
