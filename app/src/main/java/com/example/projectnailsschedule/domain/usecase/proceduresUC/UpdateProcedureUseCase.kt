package com.example.projectnailsschedule.domain.usecase.proceduresUC

import com.example.projectnailsschedule.domain.models.ProcedureModelDb
import com.example.projectnailsschedule.domain.repository.repo.ProcedureRepository

class UpdateProcedureUseCase(private val procedureRepository: ProcedureRepository) {

    suspend fun execute(procedureModelDb: ProcedureModelDb): Boolean {
        procedureRepository.updateProcedure(procedureModelDb)
        return true
    }
}