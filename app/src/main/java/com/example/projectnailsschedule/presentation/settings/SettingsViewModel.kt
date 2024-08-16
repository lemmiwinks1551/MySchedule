package com.example.projectnailsschedule.presentation.settings

import androidx.lifecycle.ViewModel
import com.example.projectnailsschedule.domain.models.FaqModel
import com.example.projectnailsschedule.domain.usecase.apiUC.GetFaqUseCase
import com.example.projectnailsschedule.domain.usecase.util.RestartAppUseCase
import com.example.projectnailsschedule.domain.usecase.settingsUC.*
import com.example.projectnailsschedule.domain.usecase.util.UpdateUserDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val setLightThemeUseCase: SetLightThemeUseCase,
    private val setDarkThemeUseCase: SetDarkThemeUseCase,
    private val getThemeUseCase: GetThemeUseCase,
    private val getLanguageUseCase: GetLanguageUseCase,
    private val setLanguageUseCase: SetLanguageUseCase,
    private val setUserThemeUseCase: SetUserThemeUseCase,
    private val getUserThemeUseCase: GetUserThemeUseCase,
    private var restartAppUseCase: RestartAppUseCase,
    private var updateUserDataUseCase: UpdateUserDataUseCase,
    private var getFaqUseCase: GetFaqUseCase
    ) : ViewModel() {

    init {
        //loadTheme()
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

    fun setUserTheme(theme: String) {
        setUserThemeUseCase.execute(theme)
    }

    fun getUserTheme() : String {
        return getUserThemeUseCase.execute()
    }

    fun restartApp() {
        restartAppUseCase.execute()
    }

    fun updateUserData(event: String) {
        updateUserDataUseCase.execute(event)
    }

    suspend fun getFaq(): List<FaqModel> {
        return getFaqUseCase.execute()
    }
}