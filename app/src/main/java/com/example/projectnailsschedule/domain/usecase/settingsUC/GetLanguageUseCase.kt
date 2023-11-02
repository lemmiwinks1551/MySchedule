package com.example.projectnailsschedule.domain.usecase.settingsUC

import com.example.projectnailsschedule.domain.repository.SettingsRepository

class GetLanguageUseCase(private val settingsRepository: SettingsRepository) {

    fun execute(): String {
        return settingsRepository.getLanguage()
    }
}