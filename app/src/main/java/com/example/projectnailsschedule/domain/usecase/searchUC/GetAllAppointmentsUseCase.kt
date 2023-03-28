package com.example.projectnailsschedule.domain.usecase.searchUC

import android.database.Cursor
import com.example.projectnailsschedule.domain.repository.ScheduleRepository

class GetAllAppointmentsUseCase(private val scheduleRepository: ScheduleRepository) {
    fun execute(): Cursor {
        return scheduleRepository.getAllAppointments()
    }
}