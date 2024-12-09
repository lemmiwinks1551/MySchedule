package com.example.projectnailsschedule.domain.usecase.sharedPref

import com.example.projectnailsschedule.domain.repository.repo.SettingsRepository

class GetCalendarDateLastUpdateUseCase(private var settingsRepository: SettingsRepository) {

    fun execute(): Long {
        return settingsRepository.getCalendarDateLastUpdate()
    }
}