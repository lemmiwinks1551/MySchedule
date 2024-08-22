package com.example.projectnailsschedule.domain.usecase.settingsUC

import com.example.projectnailsschedule.domain.repository.repo.SettingsRepository

class GetUserThemeUseCase(private val settingsRepository: SettingsRepository) {

    fun execute(): String {
        return settingsRepository.getUserTheme()
    }
}