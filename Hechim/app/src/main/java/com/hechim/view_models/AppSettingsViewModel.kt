package com.hechim.view_models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.hechim.models.repo.AppSettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppSettingsViewModel @Inject constructor(
    private val appSettingsRepository: AppSettingsRepository
):ViewModel() {


    val settings = appSettingsRepository.readAppSettings.asLiveData()

    fun updateDarkTheme(value: Boolean) {
        viewModelScope.launch {
            appSettingsRepository.updateDarkTheme(value)
        }
    }

    fun updateOnBoarding(value: Boolean) {
        viewModelScope.launch {
            appSettingsRepository.updateOnBoarding(value)
        }
    }
}