package com.example.projectnailsschedule.presentation.appointment.selectClient

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.projectnailsschedule.domain.models.ClientModelDb
import com.example.projectnailsschedule.domain.usecase.clientsUC.SearchClientUseCase

class SelectClientViewModel(
    private val searchClientUseCase: SearchClientUseCase
): ViewModel() {

    fun searchClients(searchQuery: String): LiveData<List<ClientModelDb>> {
        return searchClientUseCase.execute(searchQuery)
    }
}