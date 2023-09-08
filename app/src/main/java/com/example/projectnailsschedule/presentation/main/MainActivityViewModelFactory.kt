package com.example.projectnailsschedule.presentation.main

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.projectnailsschedule.data.repository.SettingsRepositoryImpl
import com.example.projectnailsschedule.domain.usecase.calendarUC.SetSelectedDateUc
import com.example.projectnailsschedule.domain.usecase.calendarUC.SetSelectedMonthUc
import com.example.projectnailsschedule.domain.usecase.settingsUC.LoadThemeUseCase

class MainActivityViewModelFactory(context: Context?) : ViewModelProvider.Factory {
    private val settingsRepositoryImpl = SettingsRepositoryImpl(context = context)

    private val loadThemeUseCase = LoadThemeUseCase(settingsRepository = settingsRepositoryImpl)

    private var setSelectedMonthUc =
        SetSelectedMonthUc(settingsRepository = settingsRepositoryImpl)

    private var setSelectedDateUc =
        SetSelectedDateUc(settingsRepository = settingsRepositoryImpl)

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(
            loadThemeUseCase = loadThemeUseCase,
            setSelectedMonthUc = setSelectedMonthUc,
            setSelectedDateUc = setSelectedDateUc
        ) as T
    }
}