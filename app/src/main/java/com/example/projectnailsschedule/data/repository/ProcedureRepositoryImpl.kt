package com.example.projectnailsschedule.data.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.example.projectnailsschedule.data.storage.ProceduresDb
import com.example.projectnailsschedule.domain.models.ProcedureModelDb
import com.example.projectnailsschedule.domain.repository.ProcedureRepository

class ProcedureRepositoryImpl(context: Context): ProcedureRepository {
    private var procedureDb = ProceduresDb.getDb(context)

    override suspend fun insertProcedure(procedureModelDb: ProcedureModelDb): Boolean {
        procedureDb.getDao().insert(procedureModelDb)
        return true
    }

    override suspend fun updateProcedure(procedureModelDb: ProcedureModelDb): Boolean {
        procedureDb.getDao().update(procedureModelDb)
        return true
    }

    override suspend fun deleteProcedure(procedureModelDb: ProcedureModelDb): Boolean {
        procedureDb.getDao().delete(procedureModelDb)
        return true
    }

    override suspend fun searchProcedure(searchQuery: String): MutableList<ProcedureModelDb> {
        return procedureDb.getDao().searchDatabase(searchQuery)
    }
}