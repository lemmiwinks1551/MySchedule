package com.example.projectnailsschedule.presentation.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.projectnailsschedule.domain.models.AppointmentModelDb
import com.example.projectnailsschedule.domain.usecase.appointmentUC.InsertAppointmentUseCase
import com.example.projectnailsschedule.domain.usecase.dateUC.DeleteAppointmentUseCase
import com.example.projectnailsschedule.domain.usecase.searchUC.GetAllAppointmentsLiveDataUseCase
import com.example.projectnailsschedule.domain.usecase.searchUC.SearchAppointmentUseCase
import com.example.projectnailsschedule.domain.usecase.socUC.*
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private var insertAppointmentUseCase: InsertAppointmentUseCase,
    private var deleteAppointmentUseCase: DeleteAppointmentUseCase,
    private var searchAppointmentUseCase: SearchAppointmentUseCase,
    private var getAllAppointmentsLiveDataUseCase: GetAllAppointmentsLiveDataUseCase,
    private val startVkUc: StartVkUc,
    private val startTelegramUc: StartTelegramUc,
    private val startInstagramUc: StartInstagramUc,
    private val startWhatsAppUc: StartWhatsAppUc,
    private val startPhoneUc: StartPhoneUc
) : ViewModel() {

    var appointmentsTotalCount = MutableLiveData<Int>()

    fun searchDatabase(searchQuery: String): LiveData<MutableList<AppointmentModelDb>> {
        return searchAppointmentUseCase.execute(searchQuery)
    }

    fun getAllAppointmentsLiveData() : LiveData<List<AppointmentModelDb>> {
        getAllAppointmentsLiveDataUseCase.execute().value?.size
        return getAllAppointmentsLiveDataUseCase.execute()
    }

    suspend fun deleteAppointment(appointmentModelDb: AppointmentModelDb) {
        deleteAppointmentUseCase.execute(appointmentModelDb)
    }

    suspend fun saveAppointment(appointmentModelDb: AppointmentModelDb) {
        insertAppointmentUseCase.execute(appointmentModelDb)
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