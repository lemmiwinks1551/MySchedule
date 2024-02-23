package com.example.projectnailsschedule.domain.usecase.appointmentUC

import com.example.projectnailsschedule.domain.models.AppointmentModelDb
import com.example.projectnailsschedule.domain.repository.ScheduleRepository

class InsertAppointmentUseCase(private val scheduleRepository: ScheduleRepository) {

    suspend fun execute(appointmentModelDb: AppointmentModelDb): Long {
        return scheduleRepository.insertAppointment(appointmentModelDb)
    }
}