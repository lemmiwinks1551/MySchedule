package com.example.projectnailsschedule.domain.usecase.settingsUC

import com.example.projectnailsschedule.domain.repository.SettingsRepository

class SetLanguageUc(private val settingsRepository: SettingsRepository) {

    fun execute(language: String) {
        settingsRepository.setLanguage(language)
    }
}