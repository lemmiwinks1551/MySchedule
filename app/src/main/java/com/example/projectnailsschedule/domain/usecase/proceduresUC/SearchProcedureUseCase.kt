package com.example.projectnailsschedule.domain.usecase.proceduresUC

import com.example.projectnailsschedule.domain.models.ProcedureModelDb
import com.example.projectnailsschedule.domain.repository.ProcedureRepository

class SearchProcedureUseCase(private val procedureRepository: ProcedureRepository) {

    suspend fun execute(searchQuery: String): MutableList<ProcedureModelDb> {
        return procedureRepository.searchProcedure(searchQuery)
    }
}