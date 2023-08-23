package com.example.projectnailsschedule.presentation.appointment.selectProcedure

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.projectnailsschedule.domain.models.ProcedureModelDb
import com.example.projectnailsschedule.domain.usecase.proceduresUc.SearchProcedureUseCase

class SelectProcedureViewModel(
    private val searchProcedureUseCase: SearchProcedureUseCase
) : ViewModel() {

    fun searchProcedures(searchQuery: String): LiveData<List<ProcedureModelDb>> {
        return searchProcedureUseCase.execute(searchQuery)
    }
}