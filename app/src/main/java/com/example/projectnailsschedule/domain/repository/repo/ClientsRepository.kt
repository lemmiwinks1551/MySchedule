package com.example.projectnailsschedule.domain.repository.repo

import com.example.projectnailsschedule.domain.models.ClientModelDb

interface ClientsRepository {

    suspend fun insertClient(clientModelDb: ClientModelDb): Long

    suspend fun updateClient(clientModelDb: ClientModelDb): Boolean

    suspend fun deleteClient(clientModelDb: ClientModelDb): Boolean

    suspend fun searchClient(searchQuery: String): MutableList<ClientModelDb>

    suspend fun getClientById(id: Long): ClientModelDb
}