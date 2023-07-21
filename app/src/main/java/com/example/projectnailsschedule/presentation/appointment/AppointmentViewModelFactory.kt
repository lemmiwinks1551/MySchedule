package com.example.projectnailsschedule.presentation.appointment

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.projectnailsschedule.data.repository.ScheduleRepositoryImpl
import com.example.projectnailsschedule.domain.usecase.appointmentUC.UpdateAppointmentUseCase
import com.example.projectnailsschedule.domain.usecase.appointmentUC.SaveAppointmentUseCase

class AppointmentViewModelFactory(context: Context?) : ViewModelProvider.Factory {
    private val scheduleRepositoryImpl = ScheduleRepositoryImpl(context = context!!)

    private val saveAppointmentUseCase =
        SaveAppointmentUseCase(scheduleRepository = scheduleRepositoryImpl)

    private val editAppointmentUseCase =
        UpdateAppointmentUseCase(scheduleRepository = scheduleRepositoryImpl)

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AppointmentViewModel(
            saveAppointmentUseCase = saveAppointmentUseCase,
            editAppointmentUseCase = editAppointmentUseCase
        ) as T
    }
}