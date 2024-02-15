package com.example.projectnailsschedule.domain.usecase.searchUC

import androidx.lifecycle.LiveData
import com.example.projectnailsschedule.domain.models.AppointmentModelDb
import com.example.projectnailsschedule.domain.repository.ScheduleRepository

class GetAllAppointmentsLiveDataUseCase(private val scheduleRepository: ScheduleRepository) {

    fun execute(): LiveData<List<AppointmentModelDb>> {
        return scheduleRepository.selectAllAppointmentsLiveData()
    }
}