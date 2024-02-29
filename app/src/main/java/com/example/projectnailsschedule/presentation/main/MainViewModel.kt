package com.example.projectnailsschedule.presentation.main

import androidx.lifecycle.ViewModel
import com.example.projectnailsschedule.domain.usecase.settingsUC.GetLanguageUseCase
import com.example.projectnailsschedule.domain.usecase.settingsUC.GetUserThemeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getLanguageUseCase: GetLanguageUseCase,
    private val getUserThemeUseCase: GetUserThemeUseCase
) : ViewModel() {

    fun getLanguage(): String {
        return getLanguageUseCase.execute()
    }

    fun getUserTheme(): String {
        return getUserThemeUseCase.execute()
    }

}


