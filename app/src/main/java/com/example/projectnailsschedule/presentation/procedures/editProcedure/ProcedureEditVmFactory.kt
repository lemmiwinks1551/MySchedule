package com.example.projectnailsschedule.presentation.procedures.editProcedure

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.projectnailsschedule.data.repository.ProcedureRepositoryImpl
import com.example.projectnailsschedule.domain.usecase.proceduresUc.InsertProcedureUseCase
import com.example.projectnailsschedule.domain.usecase.proceduresUc.UpdateProcedureUseCase

class ProcedureEditVmFactory(context: Context?) : ViewModelProvider.Factory {
    private val proceduresRepositoryImpl = ProcedureRepositoryImpl(context = context!!)

    private var insertProcedureUseCase =
        InsertProcedureUseCase(procedureRepository = proceduresRepositoryImpl)

    private var updateProcedureUseCase =
        UpdateProcedureUseCase(procedureRepository = proceduresRepositoryImpl)

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ProcedureEditViewModel(
            insertProcedureUseCase = insertProcedureUseCase,
            updateProcedureUseCase = updateProcedureUseCase
        ) as T
    }
}