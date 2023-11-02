package com.example.projectnailsschedule.presentation.settings

import androidx.lifecycle.ViewModel
import com.example.projectnailsschedule.domain.usecase.settingsUC.*
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val setLightThemeUseCase: SetLightThemeUseCase,
    private val setDarkThemeUseCase: SetDarkThemeUseCase,
    private val getThemeUseCase: GetThemeUseCase,
    private val getLanguageUseCase: GetLanguageUseCase,
    private val setLanguageUseCase: SetLanguageUseCase
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