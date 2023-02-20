package com.example.projectnailsschedule.domain.usecase.searchUC

import android.database.Cursor
import com.example.projectnailsschedule.domain.repository.ScheduleRepository

class SearchAppointmentsUseCase(private val scheduleRepository: ScheduleRepository) {
    fun execute(searchString: ArrayList<String>): Cursor {
        return scheduleRepository.searchAppointment(searchString)
    }
}