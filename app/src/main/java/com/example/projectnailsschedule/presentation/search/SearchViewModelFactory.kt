package com.example.projectnailsschedule.presentation.search

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.projectnailsschedule.data.repository.ScheduleRepositoryImpl
import com.example.projectnailsschedule.domain.usecase.appointmentUC.GetAllAppointmentsUseCase
import com.example.projectnailsschedule.domain.usecase.appointmentUC.SaveAppointmentUseCase
import com.example.projectnailsschedule.domain.usecase.dateUC.DeleteAppointmentUseCase
import com.example.projectnailsschedule.domain.usecase.searchUC.GetAllAppointmentsLiveDataUseCase
import com.example.projectnailsschedule.domain.usecase.searchUC.SearchAppointmentUC
import com.example.projectnailsschedule.domain.usecase.socUC.*

class SearchViewModelFactory(context: Context?) : ViewModelProvider.Factory {
    private val scheduleRepositoryImpl = ScheduleRepositoryImpl(context = context!!)

    private var deleteAppointmentUseCase =
        DeleteAppointmentUseCase(scheduleRepository = scheduleRepositoryImpl)

    private var saveAppointmentsUseCase =
        SaveAppointmentUseCase(scheduleRepository = scheduleRepositoryImpl)

    private var searchAppointmentUC =
        SearchAppointmentUC(scheduleRepository = scheduleRepositoryImpl)

    private var getAllAppointmentsLiveDataUseCase =
        GetAllAppointmentsLiveDataUseCase(scheduleRepository = scheduleRepositoryImpl)

    private var startVkUc = StartVkUc(context!!)
    private var startTelegramUc = StartTelegramUc(context!!)
    private var startInstagramUc = StartInstagramUc(context!!)
    private var startWhatsAppUc = StartWhatsAppUc(context!!)
    private var startPhoneUc = StartPhoneUc(context!!)

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SearchViewModel(
            saveAppointmentUseCase = saveAppointmentsUseCase,
            deleteAppointmentUseCase = deleteAppointmentUseCase,
            searchAppointmentUC = searchAppointmentUC,
            getAllAppointmentsLiveDataUseCase = getAllAppointmentsLiveDataUseCase,
            startVkUc = startVkUc,
            startTelegramUc = startTelegramUc,
            startInstagramUc = startInstagramUc,
            startWhatsAppUc = startWhatsAppUc,
            startPhoneUc = startPhoneUc
        ) as T
    }
}