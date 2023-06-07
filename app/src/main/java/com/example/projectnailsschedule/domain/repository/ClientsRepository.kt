package com.example.projectnailsschedule.domain.repository

import com.example.projectnailsschedule.domain.models.ClientModelDb

interface ClientsRepository {

    fun saveClient(clientModelDb: ClientModelDb): Boolean

    fun updateClient(clientModelDb: ClientModelDb): Boolean

    fun deleteClient(clientModelDb: ClientModelDb)

    fun getAllClients(): List<ClientModelDb>
}