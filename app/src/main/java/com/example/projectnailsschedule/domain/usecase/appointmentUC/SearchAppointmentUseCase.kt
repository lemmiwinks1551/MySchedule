package com.example.projectnailsschedule.domain.usecase.appointmentUC

import com.example.projectnailsschedule.domain.models.AppointmentModelDb
import com.example.projectnailsschedule.domain.repository.repo.ScheduleRepository

class SearchAppointmentUseCase(private val scheduleRepository: ScheduleRepository) {

    suspend fun execute(searchQuery: String): MutableList<AppointmentModelDb> {
        return scheduleRepository.searchAppointment(searchQuery)
    }
}