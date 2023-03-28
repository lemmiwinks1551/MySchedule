package com.example.projectnailsschedule.domain.repository

interface SettingsRepository {

    fun setDarkTheme()

    fun setLightTheme()

    fun loadTheme(): Boolean
}