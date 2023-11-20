package com.example.projectnailsschedule.domain.usecase.proceduresUC

import com.example.projectnailsschedule.domain.models.ProcedureModelDb
import com.example.projectnailsschedule.domain.repository.ProcedureRepository

class InsertProcedureUseCase(private val procedureRepository: ProcedureRepository) {

    suspend fun execute(procedureModelDb: ProcedureModelDb): Boolean {
        procedureRepository.insertProcedure(procedureModelDb)
        return true
    }
}