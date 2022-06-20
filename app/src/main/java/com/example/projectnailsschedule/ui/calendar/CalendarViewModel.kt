package com.example.projectnailsschedule.ui.calendar

import android.util.Log
import androidx.lifecycle.ViewModel

class CalendarViewModel : ViewModel() {
    init {
        Log.e("LifeCycle", "CalendarViewModel created")
    }

    override fun onCleared() {
        Log.e("LifeCycle", "CalendarViewModel cleared")
        super.onCleared()
    }

    fun calendarChanged(day: String) {
        Log.e("Calendar", day)
    }
}