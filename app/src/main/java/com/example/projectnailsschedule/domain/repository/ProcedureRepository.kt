package com.example.projectnailsschedule.domain.repository

import androidx.lifecycle.LiveData
import com.example.projectnailsschedule.domain.models.ProcedureModelDb

interface ProcedureRepository {

    suspend fun insertProcedure(procedureModelDb: ProcedureModelDb): Boolean

    suspend fun updateProcedure(procedureModelDb: ProcedureModelDb): Boolean

    suspend fun deleteProcedure(procedureModelDb: ProcedureModelDb): Boolean

    fun searchProcedure(searchQuery: String): LiveData<List<ProcedureModelDb>>
}