package com.example.projectnailsschedule.domain.repository

import androidx.lifecycle.LiveData
import com.example.projectnailsschedule.domain.models.ClientModelDb

interface ClientsRepository {

    suspend fun insertClient(clientModelDb: ClientModelDb): Boolean

    suspend fun updateClient(clientModelDb: ClientModelDb): Boolean

    suspend fun deleteClient(clientModelDb: ClientModelDb): Boolean

    suspend fun selectAllClients(): List<ClientModelDb>

    suspend fun searchClient(searchQuery: String): LiveData<List<ClientModelDb>>
}