package com.example.projectnailsschedule.domain.repository.repo

import com.example.projectnailsschedule.domain.models.ProcedureModelDb

interface ProcedureRepository {

    suspend fun insertProcedure(procedureModelDb: ProcedureModelDb): Boolean

    suspend fun updateProcedure(procedureModelDb: ProcedureModelDb): Boolean

    suspend fun deleteProcedure(procedureModelDb: ProcedureModelDb): Boolean

    suspend fun searchProcedure(searchQuery: String): MutableList<ProcedureModelDb>
}