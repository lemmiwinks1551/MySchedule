package com.example.projectnailsschedule.presentation.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.projectnailsschedule.domain.usecase.searchUC.SearchAppointmentsUseCase

class SearchViewModel(
    private val searchAppointmentsUseCase: SearchAppointmentsUseCase
) : ViewModel() {

    var searchString = MutableLiveData<Array<String>>()

    fun searchAppointment() {
        searchAppointmentsUseCase.execute(searchString.value!!)
    }
}