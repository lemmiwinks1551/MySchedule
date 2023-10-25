package com.example.projectnailsschedule.domain.usecase.calendarUC

import com.example.projectnailsschedule.domain.repository.SettingsRepository
import java.time.LocalDate

class GetSelectedMonthUc(private val settingsRepository: SettingsRepository) {
    // Retrieves the selected month from Shared Preferences to display it on the calendar screen (in a table or list)
    fun execute(): LocalDate {
        return settingsRepository.getSelectedMonth()
    }
}