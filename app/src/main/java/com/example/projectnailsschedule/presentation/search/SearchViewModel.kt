package com.example.projectnailsschedule.presentation.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.projectnailsschedule.domain.models.AppointmentParams
import com.example.projectnailsschedule.domain.usecase.searchUC.SearchAppointmentsUseCase

class SearchViewModel(
    private val searchAppointmentsUseCase: SearchAppointmentsUseCase
) : ViewModel() {

    var searchString = ArrayList<String>()
    var appointmentArray = MutableLiveData<Array<AppointmentParams>>()

    fun searchAppointment() {
        val cursor = searchAppointmentsUseCase.execute(searchString)
    }
}