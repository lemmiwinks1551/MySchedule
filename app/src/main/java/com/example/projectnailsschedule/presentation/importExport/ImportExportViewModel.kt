package com.example.projectnailsschedule.presentation.importExport

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.example.projectnailsschedule.domain.usecase.importExportUC.ExportUseCase
import com.example.projectnailsschedule.domain.usecase.importExportUC.ImportUseCase
import com.example.projectnailsschedule.domain.usecase.importExportUC.RestartAppUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ImportExportViewModel @Inject constructor(
    private var restartAppUseCase: RestartAppUseCase,
    private var importUseCase: ImportUseCase,
    private var exportUseCase: ExportUseCase
) : ViewModel() {

    fun restartApp() {
        restartAppUseCase.execute()
    }

    fun import(treeUri: Uri) {
        importUseCase.execute(treeUri)
    }

    fun export(treeUri: Uri) {
        exportUseCase.execute(treeUri)
    }
}