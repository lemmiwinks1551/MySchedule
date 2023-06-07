package com.example.projectnailsschedule.data.repository

import android.content.Context
import android.util.Log
import com.example.projectnailsschedule.data.storage.ClientsDb
import com.example.projectnailsschedule.domain.models.ClientModelDb
import com.example.projectnailsschedule.domain.repository.ClientsRepository

class ClientRepositoryImpl(context: Context) : ClientsRepository {
    private var dbRoom = ClientsDb.getDb(context)
    private var log = this::class.simpleName

    override fun saveClient(clientModelDb: ClientModelDb): Boolean {
        // Save client in database
        val thread = Thread {
            dbRoom.getDao().insert(clientModelDb)
        }
        thread.start()
        thread.join()
        Log.e(log, "Client $clientModelDb saved")
        return true
    }

    override fun updateClient(clientModelDb: ClientModelDb): Boolean {
        // Edit appointment in database
        val thread = Thread {
            dbRoom.getDao().update(clientModelDb)
        }
        thread.start()
        thread.join()
        Log.e(log, "Appointment $clientModelDb updated")
        return true
    }

    override fun getAllClients(): List<ClientModelDb> {
        var arrayOfAppointmentModelDbs = listOf<ClientModelDb>()
        val thread = Thread {
            arrayOfAppointmentModelDbs = dbRoom.getDao().selectAllList()
        }
        thread.start()
        thread.join()

        return arrayOfAppointmentModelDbs
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