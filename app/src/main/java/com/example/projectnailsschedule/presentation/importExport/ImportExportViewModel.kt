package com.example.projectnailsschedule.presentation.importExport

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.example.projectnailsschedule.domain.usecase.importExportUc.ExportUseCase
import com.example.projectnailsschedule.domain.usecase.importExportUc.ImportUseCase
import com.example.projectnailsschedule.domain.usecase.importExportUc.RestartAppUseCase

class ImportExportViewModel(
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