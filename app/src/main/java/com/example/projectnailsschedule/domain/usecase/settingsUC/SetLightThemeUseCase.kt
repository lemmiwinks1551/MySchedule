package com.example.projectnailsschedule.domain.usecase.settingsUC

import com.example.projectnailsschedule.domain.repository.SettingsRepository

class SetLightThemeUseCase(private val settingsRepository: SettingsRepository) {

    fun execute() {
        settingsRepository.setLightTheme()
    }
}