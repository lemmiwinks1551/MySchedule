package com.example.projectnailsschedule.presentation.procedures.editProcedure

import androidx.lifecycle.ViewModel
import com.example.projectnailsschedule.domain.models.ProcedureModelDb
import com.example.projectnailsschedule.domain.usecase.proceduresUc.InsertProcedureUseCase
import com.example.projectnailsschedule.domain.usecase.proceduresUc.UpdateProcedureUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProcedureEditViewModel @Inject constructor(
    private val insertProcedureUseCase: InsertProcedureUseCase,
    private val updateProcedureUseCase: UpdateProcedureUseCase
) : ViewModel() {

    suspend fun insertProcedure(procedureModelDb: ProcedureModelDb) {
        insertProcedureUseCase.execute(procedureModelDb)
    }

    suspend fun updateProcedure(procedureModelDb: ProcedureModelDb) {
        updateProcedureUseCase.execute(procedureModelDb)
    }
}