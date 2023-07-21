package com.example.projectnailsschedule.domain.usecase.appointmentUC

import androidx.lifecycle.LiveData
import com.example.projectnailsschedule.domain.models.AppointmentModelDb
import com.example.projectnailsschedule.domain.repository.ScheduleRepository

class GetMonthAppointmentsUC(private val scheduleRepository: ScheduleRepository) {
    fun execute(dateStart: String): LiveData<List<AppointmentModelDb>> {
        return scheduleRepository.getMonthAppointments(dateStart)
    }
}