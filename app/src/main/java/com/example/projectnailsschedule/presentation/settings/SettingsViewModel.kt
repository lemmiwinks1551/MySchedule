package com.example.projectnailsschedule.presentation.settings

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.projectnailsschedule.domain.usecase.settingsUC.LoadThemeUseCase
import com.example.projectnailsschedule.domain.usecase.settingsUC.SetDarkThemeUseCase
import com.example.projectnailsschedule.domain.usecase.settingsUC.SetLightThemeUseCase

class SettingsViewModel(
    private val setLightThemeUseCase: SetLightThemeUseCase,
    private val setDarkThemeUseCase: SetDarkThemeUseCase,
    private val loadThemeUseCase: LoadThemeUseCase
) : ViewModel() {

    var darkThemeOn = MutableLiveData<Boolean>()

    fun setLightTheme() {
        setLightThemeUseCase.execute()
        darkThemeOn.value = false
    }

    fun setDarkTheme() {
        setDarkThemeUseCase.execute()
        darkThemeOn.value = true
    }
}