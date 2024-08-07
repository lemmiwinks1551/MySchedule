package com.example.projectnailsschedule.presentation.procedures

import androidx.lifecycle.ViewModel
import com.example.projectnailsschedule.domain.models.ProcedureModelDb
import com.example.projectnailsschedule.domain.usecase.proceduresUC.DeleteProcedureUseCase
import com.example.projectnailsschedule.domain.usecase.proceduresUC.InsertProcedureUseCase
import com.example.projectnailsschedule.domain.usecase.proceduresUC.SearchProcedureUseCase
import com.example.projectnailsschedule.domain.usecase.proceduresUC.UpdateProcedureUseCase
import com.example.projectnailsschedule.domain.usecase.util.UpdateUserDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProceduresViewModel @Inject constructor(
    private val insertProcedureUseCase: InsertProcedureUseCase,
    private val updateProcedureUseCase: UpdateProcedureUseCase,
    private val searchProcedureUseCase: SearchProcedureUseCase,
    private val deleteProcedureUseCase: DeleteProcedureUseCase,
    private var updateUserDataUseCase: UpdateUserDataUseCase
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

    suspend fun searchProcedure(searchQuery: String): MutableList<ProcedureModelDb> {
        return searchProcedureUseCase.execute(searchQuery)
    }

    fun updateUserData(event: String) {
        updateUserDataUseCase.execute(event)
    }
}