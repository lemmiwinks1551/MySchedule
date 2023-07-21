package com.example.projectnailsschedule.presentation.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.projectnailsschedule.domain.models.AppointmentModelDb
import com.example.projectnailsschedule.domain.usecase.appointmentUC.SaveAppointmentUseCase
import com.example.projectnailsschedule.domain.usecase.dateUC.DeleteAppointmentUseCase
import com.example.projectnailsschedule.domain.usecase.searchUC.GetAllAppointmentsLiveDataUseCase
import com.example.projectnailsschedule.domain.usecase.searchUC.SearchAppointmentUC

class SearchViewModel(
    private var saveAppointmentUseCase: SaveAppointmentUseCase,
    private var deleteAppointmentUseCase: DeleteAppointmentUseCase,
    private var searchAppointmentUC: SearchAppointmentUC,
    private var getAllAppointmentsLiveDataUseCase: GetAllAppointmentsLiveDataUseCase
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
}