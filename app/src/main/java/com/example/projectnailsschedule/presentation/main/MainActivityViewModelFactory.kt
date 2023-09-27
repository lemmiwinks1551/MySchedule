package com.example.projectnailsschedule.presentation.main

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.projectnailsschedule.data.repository.SettingsRepositoryImpl
import com.example.projectnailsschedule.domain.usecase.calendarUC.SetSelectedDateUc
import com.example.projectnailsschedule.domain.usecase.calendarUC.SetSelectedMonthUc
import com.example.projectnailsschedule.domain.usecase.settingsUC.GetLanguageUc
import com.example.projectnailsschedule.domain.usecase.settingsUC.GetThemeUseCase
import com.example.projectnailsschedule.domain.usecase.settingsUC.SetLanguageUc

class MainActivityViewModelFactory(context: Context?) : ViewModelProvider.Factory {
    private val settingsRepositoryImpl = SettingsRepositoryImpl(context = context)

    private val getThemeUseCase = GetThemeUseCase(settingsRepository = settingsRepositoryImpl)

    private var setSelectedMonthUc =
        SetSelectedMonthUc(settingsRepository = settingsRepositoryImpl)

    private var setSelectedDateUc =
        SetSelectedDateUc(settingsRepository = settingsRepositoryImpl)

    private val getLanguageUc = GetLanguageUc(settingsRepository = settingsRepositoryImpl)

    private val setLanguageUc = SetLanguageUc(settingsRepository = settingsRepositoryImpl)

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(
            setSelectedMonthUc = setSelectedMonthUc,
            setSelectedDateUc = setSelectedDateUc,
            getLanguageUc = getLanguageUc
        ) as T
    }
}