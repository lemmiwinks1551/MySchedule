package com.example.projectnailsschedule.domain.usecase.sharedPref

import com.example.projectnailsschedule.domain.repository.repo.SettingsRepository

class SetCalendarLastUpdateUseCase(private var settingsRepository: SettingsRepository) {

    fun execute(time: Long) {
        settingsRepository.setCalendarDateLastUpdate(time)
    }
}