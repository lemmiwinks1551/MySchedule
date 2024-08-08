package com.example.projectnailsschedule.presentation.importExport

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.example.projectnailsschedule.domain.usecase.importExportUC.ExportUseCase
import com.example.projectnailsschedule.domain.usecase.importExportUC.ImportUseCase
import com.example.projectnailsschedule.domain.usecase.util.RestartAppUseCase
import com.example.projectnailsschedule.domain.usecase.util.UpdateUserDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ImportExportViewModel @Inject constructor(
    private var restartAppUseCase: RestartAppUseCase,
    private var importUseCase: ImportUseCase,
    private var exportUseCase: ExportUseCase,
    private var updateUserDataUseCase: UpdateUserDataUseCase
) : ViewModel() {

    fun restartApp() {
        restartAppUseCase.execute()
    }

    fun import(treeUri: Uri) {
        updateUserData("ImportExportViewModel Importing")
        importUseCase.execute(treeUri)
        updateUserData("ImportExportViewModel Imported")
    }

    fun export(treeUri: Uri) {
        updateUserData("ImportExportViewModel Exporting")
        exportUseCase.execute(treeUri)
        updateUserData("ImportExportViewModel Exported")
    }

    fun updateUserData(event: String) {
        updateUserDataUseCase.execute(event)
    }
}