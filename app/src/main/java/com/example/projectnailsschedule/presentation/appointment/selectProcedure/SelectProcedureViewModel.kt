package com.example.projectnailsschedule.presentation.appointment.selectProcedure

import androidx.lifecycle.ViewModel
import com.example.projectnailsschedule.domain.models.ProcedureModelDb
import com.example.projectnailsschedule.domain.usecase.proceduresUC.SearchProcedureUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SelectProcedureViewModel @Inject constructor(
    private val searchProcedureUseCase: SearchProcedureUseCase
) : ViewModel() {

    suspend fun searchProcedures(searchQuery: String): MutableList<ProcedureModelDb> {
        return searchProcedureUseCase.execute(searchQuery)
    }
}