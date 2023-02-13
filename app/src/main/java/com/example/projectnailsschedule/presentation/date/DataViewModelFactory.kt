package com.example.projectnailsschedule.presentation.date

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.projectnailsschedule.data.repository.ScheduleRepositoryImpl
import com.example.projectnailsschedule.data.repository.StatusRepositoryImpl
import com.example.projectnailsschedule.domain.usecase.calendarUC.GetDateStatusUseCase
import com.example.projectnailsschedule.domain.usecase.dateUC.DeleteAppointmentUseCase
import com.example.projectnailsschedule.domain.usecase.dateUC.GetAppointmentsCountUseCase
import com.example.projectnailsschedule.domain.usecase.dateUC.SetDateStatusUseCase

/** Create Factory for Date Fragment with UseCases */


class DataViewModelFactory(context: Context?) : ViewModelProvider.Factory {

    private val statusRepositoryImpl = StatusRepositoryImpl(context = context)
    private val scheduleRepositoryImpl = ScheduleRepositoryImpl(context = context)

    private var deleteAppointmentUseCase =
        DeleteAppointmentUseCase(scheduleRepository = scheduleRepositoryImpl)

    private val setDateStatusUseCase =
        SetDateStatusUseCase(statusRepository = statusRepositoryImpl)

    private val getDateStatusUseCase =
        GetDateStatusUseCase(statusRepository = statusRepositoryImpl)

    private val getAppointmentsCountUseCase =
        GetAppointmentsCountUseCase(scheduleRepository = scheduleRepositoryImpl)

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DateViewModel (
            deleteAppointmentUseCase = deleteAppointmentUseCase,
            setDateStatusUseCase = setDateStatusUseCase,
            getDateStatusUseCase = getDateStatusUseCase,
            getAppointmentsCountUseCase = getAppointmentsCountUseCase
        ) as T
    }
}