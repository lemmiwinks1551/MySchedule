package com.example.projectnailsschedule.presentation.procedures

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.projectnailsschedule.data.repository.ProcedureRepositoryImpl
import com.example.projectnailsschedule.domain.usecase.proceduresUc.DeleteProcedureUseCase
import com.example.projectnailsschedule.domain.usecase.proceduresUc.SaveProcedureUseCase
import com.example.projectnailsschedule.domain.usecase.proceduresUc.SearchProcedureUseCase

class ProcedureVmFactory(context: Context?) : ViewModelProvider.Factory {
    private val proceduresRepositoryImpl = ProcedureRepositoryImpl(context = context!!)

    private var searchProcedureUseCase =
        SearchProcedureUseCase(procedureRepository = proceduresRepositoryImpl)
    private var deleteProcedureUseCase =
        DeleteProcedureUseCase(procedureRepository = proceduresRepositoryImpl)
    private var saveProcedureUseCase =
        SaveProcedureUseCase(procedureRepository = proceduresRepositoryImpl)

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ProceduresViewModel(
            searchProcedureUseCase = searchProcedureUseCase,
            deleteProcedureUseCase = deleteProcedureUseCase,
            saveProcedureUseCase = saveProcedureUseCase
        ) as T
    }
}