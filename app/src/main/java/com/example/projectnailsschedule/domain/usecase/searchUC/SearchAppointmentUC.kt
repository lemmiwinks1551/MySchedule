package com.example.projectnailsschedule.domain.usecase.searchUC

import androidx.lifecycle.LiveData
import com.example.projectnailsschedule.domain.models.AppointmentModelDb
import com.example.projectnailsschedule.domain.repository.ScheduleRepository

class SearchAppointmentUC(private val scheduleRepository: ScheduleRepository) {
    fun execute(searchQuery: String): LiveData<List<AppointmentModelDb>> {
        return scheduleRepository.searchAppointment(searchQuery)
    }
}