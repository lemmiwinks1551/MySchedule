package com.example.projectnailsschedule.domain.repository

import androidx.lifecycle.LiveData
import com.example.projectnailsschedule.domain.models.ProcedureModelDb

interface ProcedureRepository {

    suspend fun saveProcedure(procedureModelDb: ProcedureModelDb)

    suspend fun updateProcedure(procedureModelDb: ProcedureModelDb)

    suspend fun deleteProcedure(procedureModelDb: ProcedureModelDb)

    fun searchProcedure(searchQuery: String): LiveData<List<ProcedureModelDb>>
}