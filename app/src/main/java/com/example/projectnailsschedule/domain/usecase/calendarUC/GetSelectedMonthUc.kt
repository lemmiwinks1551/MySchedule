package com.example.projectnailsschedule.domain.usecase.calendarUC

import com.example.projectnailsschedule.domain.repository.SettingsRepository
import java.time.LocalDate

class GetSelectedMonthUc(private val settingsRepository: SettingsRepository) {

    fun execute(): LocalDate {
        return settingsRepository.getSelectedMonth()
    }
}