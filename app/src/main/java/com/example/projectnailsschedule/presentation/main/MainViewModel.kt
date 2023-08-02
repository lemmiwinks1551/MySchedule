package com.example.projectnailsschedule.presentation.main

import androidx.lifecycle.ViewModel
import com.example.projectnailsschedule.domain.usecase.calendarUC.SetSelectedMonthUc
import com.example.projectnailsschedule.domain.usecase.settingsUC.LoadThemeUseCase
import java.time.LocalDate

class MainViewModel(
    private val loadThemeUseCase: LoadThemeUseCase,
    private val setSelectedMonthUc: SetSelectedMonthUc
) : ViewModel() {

    var darkThemeOn: Boolean? = null

    fun loadTheme() {
        darkThemeOn = loadThemeUseCase.execute()
    }

/*    init {
        setSelectedMonth()
    }

    private fun setSelectedMonth() {
        setSelectedMonthUc.execute(LocalDate.now())
    }*/
}


