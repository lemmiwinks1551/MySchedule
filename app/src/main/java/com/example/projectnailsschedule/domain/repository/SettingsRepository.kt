package com.example.projectnailsschedule.domain.repository

import java.time.LocalDate

interface SettingsRepository {

    fun setDarkTheme()

    fun setLightTheme()

    fun loadTheme(): Boolean

    fun getSelectedMonth() : LocalDate

    fun setSelectedMonth(selectedDate: LocalDate)

    fun setSelectedDate(selectedDate: LocalDate)

    fun getSelectedDate(): LocalDate
}