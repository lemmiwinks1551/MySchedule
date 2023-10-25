package com.example.projectnailsschedule.domain.repository

import androidx.lifecycle.LiveData
import com.example.projectnailsschedule.domain.models.ClientModelDb

interface ClientsRepository {

    fun insertClient(clientModelDb: ClientModelDb): Boolean

    fun updateClient(clientModelDb: ClientModelDb): Boolean

    fun deleteClient(clientModelDb: ClientModelDb)

    fun getAllClients(): List<ClientModelDb>

    fun searchClient(searchQuery: String): LiveData<List<ClientModelDb>>
}