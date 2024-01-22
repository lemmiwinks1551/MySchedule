package com.example.projectnailsschedule.domain.usecase.settingsUC

import com.example.projectnailsschedule.domain.repository.SettingsRepository

class SetUserThemeUseCase(private val settingsRepository: SettingsRepository) {

    fun execute(theme: String): Boolean {
        settingsRepository.setUserTheme(theme)
        return true
    }
}