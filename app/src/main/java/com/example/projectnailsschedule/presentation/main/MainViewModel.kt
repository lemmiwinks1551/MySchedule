package com.example.projectnailsschedule.presentation.main

import androidx.lifecycle.ViewModel
import com.example.projectnailsschedule.domain.usecase.settingsUC.LoadThemeUseCase

class MainViewModel(
    private val loadThemeUseCase: LoadThemeUseCase
) : ViewModel() {

    var darkThemeOn: Boolean? = null

    fun loadTheme() {
        darkThemeOn = loadThemeUseCase.execute()
    }
}


