package com.example.projectnailsschedule.presentation.search

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.projectnailsschedule.data.repository.ScheduleRepositoryImpl
import com.example.projectnailsschedule.domain.usecase.searchUC.SearchAppointmentsUseCase

class SearchViewModelFactory(context: Context?) : ViewModelProvider.Factory {
    private val scheduleRepositoryImpl = ScheduleRepositoryImpl(context = context)

    private val searchAppointmentsUseCase =
        SearchAppointmentsUseCase(scheduleRepository = scheduleRepositoryImpl)

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SearchViewModel(
            searchAppointmentsUseCase = searchAppointmentsUseCase
        ) as T
    }
}