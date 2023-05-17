package com.example.projectnailsschedule.presentation.search

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.projectnailsschedule.data.repository.ScheduleRepositoryImpl
import com.example.projectnailsschedule.domain.usecase.dateUC.DeleteAppointmentUseCase

class SearchViewModelFactory(context: Context?) : ViewModelProvider.Factory {
    private val scheduleRepositoryImpl = ScheduleRepositoryImpl(context = context!!)

    private var deleteAppointmentUseCase =
        DeleteAppointmentUseCase(scheduleRepository = scheduleRepositoryImpl)

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SearchViewModel(
            deleteAppointmentUseCase = deleteAppointmentUseCase
        ) as T
    }
}