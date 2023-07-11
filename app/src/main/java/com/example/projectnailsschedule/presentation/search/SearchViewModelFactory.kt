package com.example.projectnailsschedule.presentation.search

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.projectnailsschedule.data.repository.ScheduleRepositoryImpl
import com.example.projectnailsschedule.domain.usecase.appointmentUC.GetAllAppointmentsUseCase
import com.example.projectnailsschedule.domain.usecase.appointmentUC.SaveAppointmentUseCase
import com.example.projectnailsschedule.domain.usecase.dateUC.DeleteAppointmentUseCase
import com.example.projectnailsschedule.domain.usecase.searchUC.GetAllAppointmentsLiveDataUseCase
import com.example.projectnailsschedule.domain.usecase.searchUC.SearchAppointmentUseCase

class SearchViewModelFactory(context: Context?) : ViewModelProvider.Factory {
    private val scheduleRepositoryImpl = ScheduleRepositoryImpl(context = context!!)

    private var deleteAppointmentUseCase =
        DeleteAppointmentUseCase(scheduleRepository = scheduleRepositoryImpl)

    private var saveAppointmentsUseCase =
        SaveAppointmentUseCase(scheduleRepository = scheduleRepositoryImpl)

    private var searchAppointmentUseCase =
        SearchAppointmentUseCase(scheduleRepository = scheduleRepositoryImpl)

    private var getAllAppointmentsUseCase =
        GetAllAppointmentsUseCase(scheduleRepository = scheduleRepositoryImpl)

    private var getAllAppointmentsLiveDataUseCase =
        GetAllAppointmentsLiveDataUseCase(scheduleRepository = scheduleRepositoryImpl)

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SearchViewModel(
            saveAppointmentUseCase = saveAppointmentsUseCase,
            deleteAppointmentUseCase = deleteAppointmentUseCase,
            searchAppointmentUseCase = searchAppointmentUseCase,
            getAllAppointmentsUseCase = getAllAppointmentsUseCase,
            getAllAppointmentsLiveDataUseCase = getAllAppointmentsLiveDataUseCase
        ) as T
    }
}