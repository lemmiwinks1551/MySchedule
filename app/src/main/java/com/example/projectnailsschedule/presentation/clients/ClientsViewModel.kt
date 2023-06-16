package com.example.projectnailsschedule.presentation.clients

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.projectnailsschedule.domain.models.ClientModelDb
import com.example.projectnailsschedule.domain.usecase.clientsUC.DeleteClientUseCase
import com.example.projectnailsschedule.domain.usecase.clientsUC.SaveClientUseCase
import com.example.projectnailsschedule.domain.usecase.clientsUC.SearchClientUseCase

class ClientsViewModel(
    private val searchClientUseCase: SearchClientUseCase,
    private val deleteClientUseCase: DeleteClientUseCase,
    private val saveClientUseCase: SaveClientUseCase
) : ViewModel() {

    fun searchDatabase(searchQuery: String): LiveData<List<ClientModelDb>> {
        return searchClientUseCase.execute(searchQuery)
    }

    fun deleteClient(clientModelDb: ClientModelDb) {
        deleteClientUseCase.execute(clientModelDb)
    }

    fun saveClient(clientModelDb: ClientModelDb) {
        saveClientUseCase.execute(clientModelDb)
    }
}