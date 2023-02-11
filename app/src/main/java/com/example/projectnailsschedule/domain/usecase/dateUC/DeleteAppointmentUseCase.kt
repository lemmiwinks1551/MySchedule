package com.example.projectnailsschedule.domain.usecase.dateUC

import com.example.projectnailsschedule.domain.models.AppointmentParams
import com.example.projectnailsschedule.domain.repository.ScheduleRepository

class DeleteAppointmentUseCase(private val scheduleRepository: ScheduleRepository) {

    fun execute(appointmentParams: AppointmentParams) {

    }
}