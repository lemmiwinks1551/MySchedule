package com.example.projectnailsschedule.presentation.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.projectnailsschedule.domain.models.AppointmentModelDb
import com.example.projectnailsschedule.domain.usecase.appointmentUC.SaveAppointmentUseCase
import com.example.projectnailsschedule.domain.usecase.dateUC.DeleteAppointmentUseCase
import com.example.projectnailsschedule.domain.usecase.searchUC.GetAllAppointmentsLiveDataUseCase
import com.example.projectnailsschedule.domain.usecase.searchUC.SearchAppointmentUC
import com.example.projectnailsschedule.domain.usecase.socUC.*

class SearchViewModel(
    private var saveAppointmentUseCase: SaveAppointmentUseCase,
    private var deleteAppointmentUseCase: DeleteAppointmentUseCase,
    private var searchAppointmentUC: SearchAppointmentUC,
    private var getAllAppointmentsLiveDataUseCase: GetAllAppointmentsLiveDataUseCase,
    private val startVkUc: StartVkUc,
    private val startTelegramUc: StartTelegramUc,
    private val startInstagramUc: StartInstagramUc,
    private val startWhatsAppUc: StartWhatsAppUc,
    private val startPhoneUc: StartPhoneUc
) : ViewModel() {

    var appointmentsTotalCount = MutableLiveData<Int>()

    fun searchDatabase(searchQuery: String): LiveData<List<AppointmentModelDb>> {
        return searchAppointmentUC.execute(searchQuery)
    }

    fun getAllAppointmentsLiveData() : LiveData<List<AppointmentModelDb>> {
        getAllAppointmentsLiveDataUseCase.execute().value?.size
        return getAllAppointmentsLiveDataUseCase.execute()
    }

    fun deleteAppointment(appointmentModelDb: AppointmentModelDb) {
        deleteAppointmentUseCase.execute(appointmentModelDb)
    }

    fun saveAppointment(appointmentModelDb: AppointmentModelDb) {
        saveAppointmentUseCase.execute(appointmentModelDb)
    }

    fun startVk(uri: String) {
        startVkUc.execute(uri)
    }

    fun startTelegram(uri: String) {
        startTelegramUc.execute(uri)
    }

    fun startInstagram(uri: String) {
        startInstagramUc.execute(uri)
    }

    fun startWhatsApp(uri: String) {
        startWhatsAppUc.execute(uri)
    }

    fun startPhone(phoneNum: String) {
        startPhoneUc.execute(phoneNum)
    }
}