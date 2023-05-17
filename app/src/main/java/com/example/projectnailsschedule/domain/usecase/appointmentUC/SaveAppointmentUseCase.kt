package com.example.projectnailsschedule.domain.usecase.appointmentUC

import com.example.projectnailsschedule.domain.models.AppointmentModelDb
import com.example.projectnailsschedule.domain.repository.ScheduleRepository

/** Save appointment in database */
class SaveAppointmentUseCase(private val scheduleRepository: ScheduleRepository) {

    fun execute(appointmentModelDb: AppointmentModelDb) {
        scheduleRepository.saveAppointment(appointmentModelDb)
    }
}