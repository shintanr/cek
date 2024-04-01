package com.shinta.githubapp.modelview

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.shinta.githubapp.helper.SettingPreference
import kotlinx.coroutines.launch

class SettingModeViewModel (private val pref: SettingPreference) : ViewModel(){
    // Mengambil preferensi pengaturan tema sebagai LiveData untuk diobservasi
    fun getThemeSettings(): LiveData<Boolean> {
        return pref.getThemeSetting().asLiveData()
    }

    // Menyimpan pengaturan tema menggunakan coroutine pada viewModelScope
    fun saveThemeSetting(isDarkModeActive: Boolean) {
        viewModelScope.launch {
            pref.saveThemeSetting(isDarkModeActive)
        }
    }
}
