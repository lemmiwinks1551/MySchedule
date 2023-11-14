package com.example.projectnailsschedule.domain.usecase.appointmentUC

import com.example.projectnailsschedule.domain.models.AppointmentModelDb
import com.example.projectnailsschedule.domain.repository.ScheduleRepository

class InsertAppointmentUseCase(private val scheduleRepository: ScheduleRepository) {

    fun execute(appointmentModelDb: AppointmentModelDb): Boolean {
        scheduleRepository.insertAppointment(appointmentModelDb)
        return true
    }
}