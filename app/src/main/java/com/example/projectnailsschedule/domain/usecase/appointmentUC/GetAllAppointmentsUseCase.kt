package com.example.projectnailsschedule.domain.usecase.appointmentUC

import com.example.projectnailsschedule.domain.models.AppointmentModelDb
import com.example.projectnailsschedule.domain.repository.ScheduleRepository

class GetAllAppointmentsUseCase(private val scheduleRepository: ScheduleRepository) {
    fun execute(): List<AppointmentModelDb> {
        return scheduleRepository.getAllAppointments()
    }
}