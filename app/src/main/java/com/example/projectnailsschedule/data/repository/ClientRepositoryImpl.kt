package com.example.projectnailsschedule.data.repository

import android.content.Context
import com.example.projectnailsschedule.data.storage.ClientsDb
import com.example.projectnailsschedule.domain.models.ClientModelDb
import com.example.projectnailsschedule.domain.repository.repo.ClientsRepository

class ClientRepositoryImpl(context: Context) : ClientsRepository {
    private var dao = ClientsDb.getDb(context).getDao()

    override suspend fun insertClient(clientModelDb: ClientModelDb): Long {
        return dao.insert(clientModelDb = clientModelDb)
    }

    override suspend fun updateClient(clientModelDb: ClientModelDb): Boolean {
        dao.update(clientModelDb = clientModelDb)
        return true
    }

    override suspend fun deleteClient(clientModelDb: ClientModelDb): Boolean {
        dao.delete(clientModelDb = clientModelDb)
        return true
    }

    override suspend fun searchClient(searchQuery: String): MutableList<ClientModelDb> {
        return dao.searchClient(searchQuery)
    }

    override suspend fun getClientById(id: Long): ClientModelDb {
        return dao.getClientById(id)
    }
}