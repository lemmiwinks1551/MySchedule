package com.example.projectnailsschedule.domain.usecase.proceduresUc

import com.example.projectnailsschedule.domain.models.ProcedureModelDb
import com.example.projectnailsschedule.domain.repository.ProcedureRepository

class SaveProcedureUseCase(private val procedureRepository: ProcedureRepository) {
    suspend fun execute(procedureModelDb: ProcedureModelDb) {
        procedureRepository.saveProcedure(procedureModelDb)
    }
}