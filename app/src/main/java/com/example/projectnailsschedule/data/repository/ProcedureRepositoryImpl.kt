package com.example.projectnailsschedule.data.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.example.projectnailsschedule.data.storage.ProceduresDb
import com.example.projectnailsschedule.domain.models.ProcedureModelDb
import com.example.projectnailsschedule.domain.repository.ProcedureRepository

class ProcedureRepositoryImpl(context: Context): ProcedureRepository {
    private var procedureDb = ProceduresDb.getDb(context)
    private var log = this::class.simpleName

    override suspend fun insertProcedure(procedureModelDb: ProcedureModelDb) {
        procedureDb.getDao().insert(procedureModelDb)
    }

    override suspend fun updateProcedure(procedureModelDb: ProcedureModelDb) {
        procedureDb.getDao().update(procedureModelDb)
    }

    override suspend fun deleteProcedure(procedureModelDb: ProcedureModelDb) {
        procedureDb.getDao().delete(procedureModelDb)
    }

    override fun searchProcedure(searchQuery: String): LiveData<List<ProcedureModelDb>> {
        return procedureDb.getDao().searchDatabase(searchQuery).asLiveData()
    }
}