package com.example.projectnailsschedule.presentation.date

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.projectnailsschedule.data.repository.ScheduleRepositoryImpl
import com.example.projectnailsschedule.data.repository.StatusRepositoryImpl
import com.example.projectnailsschedule.domain.usecase.calendarUC.GetDateStatusUseCase
import com.example.projectnailsschedule.domain.usecase.dateUC.DeleteAppointmentUseCase
import com.example.projectnailsschedule.domain.usecase.dateUC.GetDateAppointmentsUseCase
import com.example.projectnailsschedule.domain.usecase.dateUC.SetDateStatusUseCase

/** Create Factory for Date Fragment with UseCases */


class DataViewModelFactory(context: Context?) : ViewModelProvider.Factory {

    private val statusRepositoryImpl = StatusRepositoryImpl(context = context)
    private val scheduleRepositoryImpl = ScheduleRepositoryImpl(context = context)

    private val setDateStatusUseCase =
        SetDateStatusUseCase(statusRepository = statusRepositoryImpl)

    private val getDateStatusUseCase =
        GetDateStatusUseCase(statusRepository = statusRepositoryImpl)

    private var deleteAppointmentUseCase =
        DeleteAppointmentUseCase(scheduleRepository = scheduleRepositoryImpl)

    private var getDateAppointmentsUseCase =
        GetDateAppointmentsUseCase(scheduleRepository = scheduleRepositoryImpl)

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DateViewModel(
            setDateStatusUseCase = setDateStatusUseCase,
            getDateStatusUseCase = getDateStatusUseCase,
            deleteAppointmentUseCase = deleteAppointmentUseCase,
            getDateAppointmentsUseCase = getDateAppointmentsUseCase
        ) as T
    }
}