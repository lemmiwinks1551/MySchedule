package com.example.projectnailsschedule.presentation.appointment

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.projectnailsschedule.data.repository.AppointmentRepositoryImpl
import com.example.projectnailsschedule.domain.usecase.SaveAppointmentUseCase

class AppointmentViewModelFactory(context: Context?) : ViewModelProvider.Factory {
    /**
     * Creates a new instance of the given `Class`.
     *
     * @param modelClass a `Class` whose instance is requested
     * @return a newly created ViewModel
     */
    private val appointmentRepositoryImpl = AppointmentRepositoryImpl(context = context)

    private val saveAppointmentUseCase =
        SaveAppointmentUseCase(appointmentRepository = appointmentRepositoryImpl)
    private val loadAppointmentUseCase =
        SaveAppointmentUseCase(appointmentRepository = appointmentRepositoryImpl)


    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AppointmentViewModel(
            saveAppointmentUseCase = saveAppointmentUseCase,
            loadAppointmentUseCase = loadAppointmentUseCase
        ) as T
    }
}