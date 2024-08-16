package com.example.projectnailsschedule.domain.usecase.settingsUC

import com.example.projectnailsschedule.domain.repository.repo.SettingsRepository

class SetLightThemeUseCase(private val settingsRepository: SettingsRepository) {

    fun execute(): Boolean {
        settingsRepository.setLightTheme()
        return true
    }
}