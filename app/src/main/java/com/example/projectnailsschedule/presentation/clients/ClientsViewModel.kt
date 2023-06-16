package com.example.projectnailsschedule.presentation.clients

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.projectnailsschedule.domain.models.ClientModelDb
import com.example.projectnailsschedule.domain.usecase.clientsUC.DeleteClientUseCase
import com.example.projectnailsschedule.domain.usecase.clientsUC.SearchClientUseCase

class ClientsViewModel(
    private val searchClientUseCase: SearchClientUseCase,
    private val deleteClientUseCase: DeleteClientUseCase
) : ViewModel() {

    val clientsCount: MutableLiveData<Int> = MutableLiveData()

    fun searchDatabase(searchQuery: String): LiveData<List<ClientModelDb>> {
        return searchClientUseCase.execute(searchQuery)
    }

/*    fun getAllClients(): LiveData<List<ClientModelDb>> {
        return clientsDb.getDao().selectAllFlow().asLiveData()
    }*/
}