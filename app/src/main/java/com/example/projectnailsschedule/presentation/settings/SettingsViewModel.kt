package com.example.projectnailsschedule.presentation.settings

import androidx.lifecycle.ViewModel
import com.example.projectnailsschedule.domain.usecase.settingsUC.*

class SettingsViewModel(
    private val setLightThemeUseCase: SetLightThemeUseCase,
    private val setDarkThemeUseCase: SetDarkThemeUseCase,
    private val getThemeUseCase: GetThemeUseCase,
    private val getLanguageUc: GetLanguageUc,
    private val setLanguageUc: SetLanguageUc
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
        darkThemeOn = getThemeUseCase.execute()
    }

    fun setLanguage(language: String) {

    }
}