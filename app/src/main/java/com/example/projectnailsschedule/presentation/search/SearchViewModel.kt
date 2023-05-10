package com.example.projectnailsschedule.presentation.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.projectnailsschedule.data.storage.ScheduleDb
import com.example.projectnailsschedule.domain.models.AppointmentModelDb
import com.example.projectnailsschedule.domain.models.AppointmentParams
import com.example.projectnailsschedule.domain.usecase.dateUC.DeleteAppointmentUseCase
import com.example.projectnailsschedule.domain.usecase.dateUC.GetDateAppointmentsUseCase
import com.example.projectnailsschedule.domain.usecase.searchUC.GetAllAppointmentsUseCase
import java.time.LocalDate

class SearchViewModel(
    private val getAllAppointmentsUseCase: GetAllAppointmentsUseCase,
    private var deleteAppointmentUseCase: DeleteAppointmentUseCase
) : ViewModel() {


    fun deleteAppointment(appointmentModelDb: AppointmentModelDb) {
        deleteAppointmentUseCase.execute(appointmentModelDb)
    }
}