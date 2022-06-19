package com.example.projectnailsschedule.ui.calendar

import android.content.Intent
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.projectnailsschedule.DateActivity

class CalendarViewModel : ViewModel() {
    init {
        Log.e("LifeCycle", "CalendarViewModel created")
    }

    override fun onCleared() {
        Log.e("LifeCycle", "CalendarViewModel cleared")
        super.onCleared()
    }

    fun calendarChanged(year: Int,month: Int, day: Int) {
        var month1 = month + 1
        Log.e("Calendar", String.format("$day.$month1.$year"))
    }
}