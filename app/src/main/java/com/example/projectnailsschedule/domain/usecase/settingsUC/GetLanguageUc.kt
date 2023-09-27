package com.example.projectnailsschedule.domain.usecase.settingsUC

import com.example.projectnailsschedule.domain.repository.SettingsRepository

class GetLanguageUc(private val settingsRepository: SettingsRepository) {
    fun execute(): String {
        return settingsRepository.getLanguage()
    }
}