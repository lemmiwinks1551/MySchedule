package com.example.projectnailsschedule.domain.usecase.settingsUC

import com.example.projectnailsschedule.domain.repository.SettingsRepository

class SetDarkThemeUseCase(private val settingsRepository: SettingsRepository) {
    fun execute() {
        settingsRepository.setDarkTheme()
    }
}