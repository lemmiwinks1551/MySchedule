package com.example.projectnailsschedule.presentation.main

import androidx.lifecycle.ViewModel
import com.example.projectnailsschedule.domain.usecase.calendarUC.SetSelectedDateUseCase
import com.example.projectnailsschedule.domain.usecase.calendarUC.SetSelectedMonthUc
import com.example.projectnailsschedule.domain.usecase.settingsUC.GetLanguageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val setSelectedMonthUc: SetSelectedMonthUc,
    private val setSelectedDateUc: SetSelectedDateUseCase,
    private val getLanguageUseCase: GetLanguageUseCase
) : ViewModel() {

    init {
        resetSelectedMonth()
        resetSelectedDate()
    }

    private fun resetSelectedMonth() {
        setSelectedMonthUc.execute(LocalDate.now())
    }

    private fun resetSelectedDate() {
        setSelectedDateUc.execute(LocalDate.now())
    }

    fun getLanguage() : String {
        return getLanguageUseCase.execute()
    }
}


