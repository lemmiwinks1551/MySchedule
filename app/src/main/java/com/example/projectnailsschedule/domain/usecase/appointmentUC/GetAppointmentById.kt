package com.example.projectnailsschedule.domain.usecase.appointmentUC

import com.example.projectnailsschedule.domain.models.AppointmentModelDb
import com.example.projectnailsschedule.domain.repository.repo.ScheduleRepository

class GetAppointmentById(private val repository: ScheduleRepository) {

    suspend fun execute(id: Long): AppointmentModelDb? {
        return repository.getById(id)
    }
}