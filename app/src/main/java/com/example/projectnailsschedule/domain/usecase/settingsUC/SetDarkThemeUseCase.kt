package com.example.projectnailsschedule.domain.usecase.settingsUC

import com.example.projectnailsschedule.domain.repository.repo.SettingsRepository

class SetDarkThemeUseCase(private val settingsRepository: SettingsRepository) {

    fun execute(): Boolean {
        settingsRepository.setDarkTheme()
        return true
    }
}