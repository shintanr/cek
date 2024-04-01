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

        val pref = SettingPreference.getInstance(application.dataStore)
        val modeSettingViewModel = ViewModelProvider(this, SettingModeModelFactory(pref)).get(
            SettingModeViewModel::class.java
        )

        modeSettingViewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                switchTheme.isChecked = true
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                switchTheme.isChecked = false
            }
        }

        switchTheme.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            modeSettingViewModel.saveThemeSetting(isChecked)
        }
    }
}