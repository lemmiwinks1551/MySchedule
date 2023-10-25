package com.example.projectnailsschedule.domain.usecase.calendarUC

import com.example.projectnailsschedule.domain.repository.SettingsRepository
import java.time.LocalDate

class SetSelectedDateUc(private val settingsRepository: SettingsRepository) {

    fun execute(selectedDate: LocalDate) {
        settingsRepository.setSelectedDate(selectedDate)
    }
}