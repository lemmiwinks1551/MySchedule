package com.example.projectnailsschedule.presentation.calendar.fullMonthView

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.projectnailsschedule.data.repository.ScheduleRepositoryImpl
import com.example.projectnailsschedule.domain.usecase.appointmentUC.GetMonthAppointmentsUC
import com.example.projectnailsschedule.domain.usecase.appointmentUC.SaveAppointmentUseCase
import com.example.projectnailsschedule.domain.usecase.dateUC.DeleteAppointmentUseCase
import com.example.projectnailsschedule.domain.usecase.dateUC.GetDateAppointmentsUseCase

class FullMonthViewModelFactory(context: Context?) : ViewModelProvider.Factory {
    private val scheduleRepositoryImpl = ScheduleRepositoryImpl(context = context!!)

    private var deleteAppointmentUseCase =
        DeleteAppointmentUseCase(scheduleRepository = scheduleRepositoryImpl)

    private val getMonthAppointments =
        GetMonthAppointmentsUC(scheduleRepository = scheduleRepositoryImpl)

    private val saveAppointmentUseCase =
        SaveAppointmentUseCase(scheduleRepository = scheduleRepositoryImpl)

    private val getDateAppointmentsUseCase =
        GetDateAppointmentsUseCase(scheduleRepository = scheduleRepositoryImpl)

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return FullMonthViewViewModel(
            deleteAppointmentUseCase = deleteAppointmentUseCase,
            getMonthAppointmentsUseCase = getMonthAppointments,
            saveAppointmentUseCase = saveAppointmentUseCase,
            getDateAppointmentsUseCase = getDateAppointmentsUseCase
        ) as T
    }
}