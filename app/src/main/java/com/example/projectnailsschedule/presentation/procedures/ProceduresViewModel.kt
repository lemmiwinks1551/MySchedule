package com.example.projectnailsschedule.presentation.procedures

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.projectnailsschedule.domain.models.ProcedureModelDb
import com.example.projectnailsschedule.domain.usecase.proceduresUc.DeleteProcedureUseCase
import com.example.projectnailsschedule.domain.usecase.proceduresUc.InsertProcedureUseCase
import com.example.projectnailsschedule.domain.usecase.proceduresUc.SearchProcedureUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProceduresViewModel @Inject constructor(
    private val searchProcedureUseCase: SearchProcedureUseCase,
    private val deleteProcedureUseCase: DeleteProcedureUseCase,
    private val insertProcedureUseCase: InsertProcedureUseCase
) : ViewModel() {

    fun searchDatabase(searchQuery: String): LiveData<List<ProcedureModelDb>> {
        return searchProcedureUseCase.execute(searchQuery)
    }

    suspend fun deleteProcedure(procedureModelDb: ProcedureModelDb) {
        deleteProcedureUseCase.execute(procedureModelDb)
    }

    suspend fun saveProcedure(procedureModelDb: ProcedureModelDb) {
        insertProcedureUseCase.execute(procedureModelDb)
    }

}