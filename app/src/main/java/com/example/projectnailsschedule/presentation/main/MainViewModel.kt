package com.example.projectnailsschedule.presentation.main

import androidx.lifecycle.ViewModel
import com.example.projectnailsschedule.domain.usecase.calendarUC.SetSelectedDateUc
import com.example.projectnailsschedule.domain.usecase.calendarUC.SetSelectedMonthUc
import com.example.projectnailsschedule.domain.usecase.settingsUC.GetLanguageUc
import com.example.projectnailsschedule.domain.usecase.settingsUC.SetLanguageUc
import java.time.LocalDate

class MainViewModel(
    private val setSelectedMonthUc: SetSelectedMonthUc,
    private val setSelectedDateUc: SetSelectedDateUc,
    private val getLanguageUc: GetLanguageUc
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
        return getLanguageUc.execute()
    }
}


