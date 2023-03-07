package com.example.projectnailsschedule.presentation.main

import androidx.lifecycle.ViewModel
import com.example.projectnailsschedule.domain.usecase.settingsUC.LoadThemeUseCase

class MainViewModel(
    loadThemeUseCase: LoadThemeUseCase
) : ViewModel() {

    var darkThemeOn: Boolean? = null

    init {
        darkThemeOn = loadThemeUseCase.execute()
    }
}


