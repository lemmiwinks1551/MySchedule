package com.example.projectnailsschedule.domain.usecase.settingsUC

import com.example.projectnailsschedule.domain.repository.SettingsRepository

class LoadThemeUseCase(private val settingsRepository: SettingsRepository) {
    fun execute(): Boolean {
        return settingsRepository.loadTheme()
    }
}