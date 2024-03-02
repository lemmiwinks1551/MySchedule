package com.example.projectnailsschedule.data.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.example.projectnailsschedule.data.storage.ClientsDb
import com.example.projectnailsschedule.domain.models.ClientModelDb
import com.example.projectnailsschedule.domain.repository.ClientsRepository

class ClientRepositoryImpl(context: Context) : ClientsRepository {
    private var clientsDb = ClientsDb.getDb(context)

    override suspend fun insertClient(clientModelDb: ClientModelDb): Boolean {
        clientsDb.getDao().insert(clientModelDb = clientModelDb)
        return true
    }

    override suspend fun updateClient(clientModelDb: ClientModelDb): Boolean {
        clientsDb.getDao().update(clientModelDb = clientModelDb)
        return true
    }

    override suspend fun deleteClient(clientModelDb: ClientModelDb): Boolean {
        clientsDb.getDao().delete(clientModelDb = clientModelDb)
        return true
    }

    override suspend fun searchClient(searchQuery: String): MutableList<ClientModelDb> {
        return clientsDb.getDao().searchClient(searchQuery)
    }
}