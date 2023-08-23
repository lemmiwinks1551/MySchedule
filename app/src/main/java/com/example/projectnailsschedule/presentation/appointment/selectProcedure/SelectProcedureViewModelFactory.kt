package com.example.projectnailsschedule.presentation.appointment.selectProcedure

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.projectnailsschedule.data.repository.ProcedureRepositoryImpl
import com.example.projectnailsschedule.domain.usecase.proceduresUc.SearchProcedureUseCase

class SelectProcedureViewModelFactory(context: Context?) : ViewModelProvider.Factory {
    private val proceduresRepositoryImpl = ProcedureRepositoryImpl(context = context!!)

    private var searchProceduresUc = SearchProcedureUseCase(proceduresRepositoryImpl)

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SelectProcedureViewModel(
            searchProcedureUseCase = searchProceduresUc
        ) as T
    }
}