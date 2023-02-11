package com.example.projectnailsschedule.presentation.date

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.projectnailsschedule.data.repository.ScheduleRepositoryImpl
import com.example.projectnailsschedule.data.repository.StatusRepositoryImpl
import com.example.projectnailsschedule.domain.usecase.dateUC.DeleteAppointmentUseCase
import com.example.projectnailsschedule.domain.usecase.dateUC.SetDayStatusUseCase

/** Create Factory for Date Fragment with UseCases */


class DataViewModelFactory(context: Context?) : ViewModelProvider.Factory {

    private val statusRepositoryImpl = StatusRepositoryImpl(context = context)
    private val scheduleRepositoryImpl = ScheduleRepositoryImpl(context = context)

    private val deleteAppointment =
        DeleteAppointmentUseCase(scheduleRepository = scheduleRepositoryImpl)

    private val setDateStatus =
        SetDayStatusUseCase(statusRepository = statusRepositoryImpl)

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        TODO("Not yet implemented")
    }
}