package com.example.projectnailsschedule.presentation.settings

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.projectnailsschedule.data.repository.SettingsRepositoryImpl
import com.example.projectnailsschedule.domain.usecase.settingsUC.LoadThemeUseCase
import com.example.projectnailsschedule.domain.usecase.settingsUC.SetDarkThemeUseCase
import com.example.projectnailsschedule.domain.usecase.settingsUC.SetLightThemeUseCase

class SettingsViewModelFactory(context: Context?) : ViewModelProvider.Factory {
    private val settingsRepositoryImpl = SettingsRepositoryImpl(context = context)

    private val setLightThemeUseCase =
        SetLightThemeUseCase(settingsRepository = settingsRepositoryImpl)
    private val setDarkThemeUseCase =
        SetDarkThemeUseCase(settingsRepository = settingsRepositoryImpl)
    private val loadThemeUseCase = LoadThemeUseCase(settingsRepository = settingsRepositoryImpl)

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SettingsViewModel(
            setLightThemeUseCase = setLightThemeUseCase,
            setDarkThemeUseCase = setDarkThemeUseCase,
            loadThemeUseCase = loadThemeUseCase
        ) as T
    }
}