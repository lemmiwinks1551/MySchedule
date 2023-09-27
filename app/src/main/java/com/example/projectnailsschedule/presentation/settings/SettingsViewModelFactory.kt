package com.example.projectnailsschedule.presentation.settings

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.projectnailsschedule.data.repository.SettingsRepositoryImpl
import com.example.projectnailsschedule.domain.usecase.settingsUC.*

class SettingsViewModelFactory(context: Context?) : ViewModelProvider.Factory {
    private val settingsRepositoryImpl = SettingsRepositoryImpl(context = context)

    private val setLightThemeUseCase =
        SetLightThemeUseCase(settingsRepository = settingsRepositoryImpl)

    private val setDarkThemeUseCase =
        SetDarkThemeUseCase(settingsRepository = settingsRepositoryImpl)

    private val getThemeUseCase = GetThemeUseCase(settingsRepository = settingsRepositoryImpl)

    private val getLanguageUc = GetLanguageUc(settingsRepository = settingsRepositoryImpl)

    private val setLanguageUc = SetLanguageUc(settingsRepository = settingsRepositoryImpl)

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SettingsViewModel(
            setLightThemeUseCase = setLightThemeUseCase,
            setDarkThemeUseCase = setDarkThemeUseCase,
            getThemeUseCase = getThemeUseCase,
            setLanguageUc = setLanguageUc,
            getLanguageUc = getLanguageUc
        ) as T
    }
}