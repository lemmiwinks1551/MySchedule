package com.example.projectnailsschedule.presentation.importExport

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.projectnailsschedule.data.repository.SettingsRepositoryImpl

class ImportExportViewModelFactory(context: Context?) : ViewModelProvider.Factory {
    private val settingsRepositoryImpl = SettingsRepositoryImpl(context = context)


    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ImportExportViewModel(
        ) as T
    }
}