package com.example.projectnailsschedule.presentation.procedures

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.projectnailsschedule.domain.models.ProcedureModelDb
import com.example.projectnailsschedule.domain.usecase.proceduresUC.DeleteProcedureUseCase
import com.example.projectnailsschedule.domain.usecase.proceduresUC.InsertProcedureUseCase
import com.example.projectnailsschedule.domain.usecase.proceduresUC.SearchProcedureUseCase
import com.example.projectnailsschedule.domain.usecase.proceduresUC.UpdateProcedureUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProceduresViewModel @Inject constructor(
    private val insertProcedureUseCase: InsertProcedureUseCase,
    private val updateProcedureUseCase: UpdateProcedureUseCase,
    private val searchProcedureUseCase: SearchProcedureUseCase,
    private val deleteProcedureUseCase: DeleteProcedureUseCase,
) : ViewModel() {

    var selectedProcedure: ProcedureModelDb? = null

    suspend fun insertProcedure(procedureModelDb: ProcedureModelDb) {
        insertProcedureUseCase.execute(procedureModelDb)
    }

    suspend fun updateProcedure(procedureModelDb: ProcedureModelDb) {
        updateProcedureUseCase.execute(procedureModelDb)
    }

    suspend fun deleteProcedure(procedureModelDb: ProcedureModelDb) {
        deleteProcedureUseCase.execute(procedureModelDb)
    }

    fun searchProcedure(searchQuery: String): LiveData<List<ProcedureModelDb>> {
        return searchProcedureUseCase.execute(searchQuery)
    }
}