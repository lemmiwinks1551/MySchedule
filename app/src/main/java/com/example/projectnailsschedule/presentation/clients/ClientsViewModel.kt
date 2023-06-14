package com.example.projectnailsschedule.presentation.clients

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.projectnailsschedule.data.storage.ClientsDb
import com.example.projectnailsschedule.domain.models.ClientModelDb

class ClientsViewModel : ViewModel() {

    var clientsDb: ClientsDb? = null
    val clientsCount: MutableLiveData<Int> = MutableLiveData()

    fun searchDatabase(searchQuery: String): LiveData<List<ClientModelDb>>? {
        return clientsDb?.getDao()?.searchDatabase(searchQuery)?.asLiveData()
    }

    fun getAllAppointments() : LiveData<List<ClientModelDb>>? {
        return clientsDb?.getDao()?.selectAll()?.asLiveData()
    }
}