package com.example.projectnailsschedule.presentation.importExport

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.projectnailsschedule.domain.usecase.importExportUc.ExportUseCase
import com.example.projectnailsschedule.domain.usecase.importExportUc.ImportUseCase
import com.example.projectnailsschedule.domain.usecase.importExportUc.RestartAppUseCase

class ImportExportViewModelFactory(context: Context?) : ViewModelProvider.Factory {

    private var restartAppUseCase = RestartAppUseCase(context = context!!)

    private var importUseCase = ImportUseCase(context!!)

    private var exportUseCase = ExportUseCase(context!!)

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ImportExportViewModel(
            restartAppUseCase = restartAppUseCase,
            importUseCase = importUseCase,
            exportUseCase = exportUseCase
        ) as T
    }
}