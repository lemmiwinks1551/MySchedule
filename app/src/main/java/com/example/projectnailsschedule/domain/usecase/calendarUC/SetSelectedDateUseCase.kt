package com.example.projectnailsschedule.domain.usecase.calendarUC

import com.example.projectnailsschedule.domain.repository.SettingsRepository
import java.time.LocalDate

class SetSelectedDateUseCase(private val settingsRepository: SettingsRepository) {

    fun execute(selectedDate: LocalDate): Boolean {
        settingsRepository.setSelectedDate(selectedDate)
        return true
    }
}