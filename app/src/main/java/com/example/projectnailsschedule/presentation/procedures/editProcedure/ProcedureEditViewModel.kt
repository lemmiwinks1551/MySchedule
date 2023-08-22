package com.example.projectnailsschedule.presentation.procedures.editProcedure

import androidx.lifecycle.ViewModel
import com.example.projectnailsschedule.domain.models.ProcedureModelDb
import com.example.projectnailsschedule.domain.usecase.proceduresUc.SaveProcedureUseCase
import com.example.projectnailsschedule.domain.usecase.proceduresUc.UpdateProcedureUseCase

class ProcedureEditViewModel(
    private val saveProcedureUseCase: SaveProcedureUseCase,
    private val updateProcedureUseCase: UpdateProcedureUseCase
) : ViewModel() {

    suspend fun saveProcedure(procedureModelDb: ProcedureModelDb) {
        saveProcedureUseCase.execute(procedureModelDb)
    }

    suspend fun updateProcedure(procedureModelDb: ProcedureModelDb) {
        updateProcedureUseCase.execute(procedureModelDb)
    }
}