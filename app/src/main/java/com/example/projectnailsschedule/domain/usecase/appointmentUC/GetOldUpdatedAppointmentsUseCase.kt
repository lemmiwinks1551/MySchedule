package com.example.projectnailsschedule.domain.usecase.appointmentUC

import com.example.projectnailsschedule.domain.models.AppointmentModelDb
import com.example.projectnailsschedule.domain.repository.repo.ScheduleRepository

class GetOldUpdatedAppointmentsUseCase(private var scheduleRepository: ScheduleRepository) {

    suspend fun execute(): List<AppointmentModelDb> {
        return scheduleRepository.getOldUpdatedAppointments()
    }
}