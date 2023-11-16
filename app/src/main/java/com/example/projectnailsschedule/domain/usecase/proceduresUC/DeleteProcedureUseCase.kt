package com.example.projectnailsschedule.domain.usecase.proceduresUC

import com.example.projectnailsschedule.domain.models.ProcedureModelDb
import com.example.projectnailsschedule.domain.repository.ProcedureRepository

class DeleteProcedureUseCase(private val procedureRepository: ProcedureRepository) {

    suspend fun execute(procedureModelDb: ProcedureModelDb): Boolean {
        procedureRepository.deleteProcedure(procedureModelDb)
        return true
    }
}