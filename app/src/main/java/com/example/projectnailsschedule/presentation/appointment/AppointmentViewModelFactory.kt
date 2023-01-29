package com.example.projectnailsschedule.presentation.appointment

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.projectnailsschedule.data.repository.AppointmentRepositoryImpl
import com.example.projectnailsschedule.domain.usecase.appointmentUC.EditAppointmentUseCase
import com.example.projectnailsschedule.domain.usecase.appointmentUC.SaveAppointmentUseCase

class AppointmentViewModelFactory(context: Context?) : ViewModelProvider.Factory {
    /**
     * Creates a new instance of the given `Class`.
     *
     * @param modelClass a `Class` whose instance is requested
     * @return a newly created ViewModel
     */
    private val appointmentRepository = AppointmentRepositoryImpl(context = context)

    private val saveAppointmentUseCase =
        SaveAppointmentUseCase(appointmentRepository = appointmentRepository)
    private val editAppointmentUseCase =
        EditAppointmentUseCase(appointmentRepository = appointmentRepository)


    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AppointmentViewModel(
            saveAppointmentUseCase = saveAppointmentUseCase,
            editAppointmentUseCase = editAppointmentUseCase
        ) as T
    }
}