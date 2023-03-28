package com.example.projectnailsschedule.domain.usecase.dateUC;

import android.database.Cursor
import com.example.projectnailsschedule.domain.models.DateParams
import com.example.projectnailsschedule.domain.repository.ScheduleRepository;

class GetDateAppointmentsUseCase(private val scheduleRepository: ScheduleRepository) {

    fun execute(dateParams: DateParams): Cursor {
        return scheduleRepository.getDateAppointments(dateParams)
    }
}
