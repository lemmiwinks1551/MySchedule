package com.example.projectnailsschedule.domain.usecase.proceduresUc

import androidx.lifecycle.LiveData
import com.example.projectnailsschedule.domain.models.ProcedureModelDb
import com.example.projectnailsschedule.domain.repository.ProcedureRepository

class SearchProcedureUseCase(private val procedureRepository: ProcedureRepository) {

    fun execute(searchQuery: String): LiveData<List<ProcedureModelDb>> {
        return procedureRepository.searchProcedure(searchQuery)
    }
}