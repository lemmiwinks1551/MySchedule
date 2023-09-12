package com.example.projectnailsschedule.presentation.main

import androidx.lifecycle.ViewModel
import com.example.projectnailsschedule.domain.usecase.calendarUC.SetSelectedDateUc
import com.example.projectnailsschedule.domain.usecase.calendarUC.SetSelectedMonthUc
import java.time.LocalDate

class MainViewModel(
    private val setSelectedMonthUc: SetSelectedMonthUc,
    private val setSelectedDateUc: SetSelectedDateUc
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
}


