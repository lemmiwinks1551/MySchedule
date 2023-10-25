package com.example.projectnailsschedule.data.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.example.projectnailsschedule.data.storage.ClientsDb
import com.example.projectnailsschedule.domain.models.ClientModelDb
import com.example.projectnailsschedule.domain.repository.ClientsRepository

class ClientRepositoryImpl(context: Context) : ClientsRepository {
    private var dbRoom = ClientsDb.getDb(context)
    private var log = this::class.simpleName

    override fun insertClient(clientModelDb: ClientModelDb): Boolean {
        // Save client in database
        val thread = Thread {
            dbRoom.getDao().insert(clientModelDb)
        }
        thread.start()
        thread.join()
        Log.e(log, "Client $clientModelDb inserted")
        return true
    }

    override fun updateClient(clientModelDb: ClientModelDb): Boolean {
        // Edit appointment in database
        val thread = Thread {
            dbRoom.getDao().update(clientModelDb)
        }
        thread.start()
        thread.join()
        Log.e(log, "Client $clientModelDb updated")
        return true
    }

    override fun getAllClients(): List<ClientModelDb> {
        var arrayOfClientsModelDbs = listOf<ClientModelDb>()
        val thread = Thread {
            arrayOfClientsModelDbs = dbRoom.getDao().selectAllList()
        }
        thread.start()
        thread.join()

        return arrayOfClientsModelDbs
    }

    override fun searchClient(searchQuery: String): LiveData<List<ClientModelDb>> {
        return dbRoom.getDao().searchDatabase(searchQuery).asLiveData()
    }

    override fun deleteClient(clientModelDb: ClientModelDb) {
        val thread = Thread {
            dbRoom.getDao().delete(clientModelDb)
        }
        thread.start()
        thread.join()
        Log.e(log, "$clientModelDb deleted")
    }
}