package com.example.projectnailsschedule.presentation.date

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.projectnailsschedule.data.repository.ScheduleRepositoryImpl
import com.example.projectnailsschedule.domain.usecase.appointmentUC.SaveAppointmentUseCase
import com.example.projectnailsschedule.domain.usecase.dateUC.DeleteAppointmentUseCase
import com.example.projectnailsschedule.domain.usecase.dateUC.GetDateAppointmentsUseCase

/** Create Factory for Date Fragment with UseCases */

class DateViewModelFactory(context: Context?) : ViewModelProvider.Factory {

    private val scheduleRepositoryImpl = ScheduleRepositoryImpl(context = context!!)

    private var deleteAppointmentUseCase =
        DeleteAppointmentUseCase(scheduleRepository = scheduleRepositoryImpl)

    private var getDateAppointmentsUseCase =
        GetDateAppointmentsUseCase(scheduleRepository = scheduleRepositoryImpl)

    private var saveAppointmentsUseCase =
        SaveAppointmentUseCase(scheduleRepository = scheduleRepositoryImpl)

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DateViewModel(
            deleteAppointmentUseCase = deleteAppointmentUseCase,
            getDateAppointmentsUseCase = getDateAppointmentsUseCase,
            saveAppointmentUseCase = saveAppointmentsUseCase
        ) as T
    }
}