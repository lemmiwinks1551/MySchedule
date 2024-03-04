package com.example.projectnailsschedule.domain.usecase.appointmentUC

import com.example.projectnailsschedule.domain.models.AppointmentModelDb
import com.example.projectnailsschedule.domain.repository.ScheduleRepository
import java.time.LocalDate

class GetDateAppointmentsUseCase(private val scheduleRepository: ScheduleRepository) {

    suspend fun execute(date: LocalDate): List<AppointmentModelDb> {
        return scheduleRepository.getDateAppointments(date)
    }
}
