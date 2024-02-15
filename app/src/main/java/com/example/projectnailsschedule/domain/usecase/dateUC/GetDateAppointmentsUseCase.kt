package com.example.projectnailsschedule.domain.usecase.dateUC;

import com.example.projectnailsschedule.domain.models.AppointmentModelDb
import com.example.projectnailsschedule.domain.models.DateParams
import com.example.projectnailsschedule.domain.repository.ScheduleRepository

class GetDateAppointmentsUseCase(private val scheduleRepository: ScheduleRepository) {

    suspend fun execute(dateParams: DateParams): Array<AppointmentModelDb> {
        return scheduleRepository.getDateAppointments(dateParams)
    }
}
