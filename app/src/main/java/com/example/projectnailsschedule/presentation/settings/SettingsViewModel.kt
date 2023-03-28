package com.example.projectnailsschedule.presentation.settings

import androidx.lifecycle.ViewModel
import com.example.projectnailsschedule.domain.usecase.settingsUC.LoadThemeUseCase
import com.example.projectnailsschedule.domain.usecase.settingsUC.SetDarkThemeUseCase
import com.example.projectnailsschedule.domain.usecase.settingsUC.SetLightThemeUseCase

class SettingsViewModel(
    private val setLightThemeUseCase: SetLightThemeUseCase,
    private val setDarkThemeUseCase: SetDarkThemeUseCase,
    private val loadThemeUseCase: LoadThemeUseCase
) : ViewModel() {

    init {
        loadTheme()
    }

    var darkThemeOn: Boolean? = null

    fun setLightTheme() {
        setLightThemeUseCase.execute()
        darkThemeOn = false
    }

    fun setDarkTheme() {
        setDarkThemeUseCase.execute()
        darkThemeOn = true
    }

    private fun loadTheme() {
        darkThemeOn = loadThemeUseCase.execute()
    }
}