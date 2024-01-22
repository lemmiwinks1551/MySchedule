package com.example.projectnailsschedule.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.example.projectnailsschedule.domain.repository.SettingsRepository
import java.time.LocalDate

class SettingsRepositoryImpl(context: Context?) : SettingsRepository {

    private val CUSTOM_PREF_NAME = "Settings"
    private val themeKey = "theme"
    private val monthKey = "month"
    private val selectedDateKey = "selectedDate"
    private val languageKey = "language"
    private val userTheme = "theme"

    private val sharedPreference: SharedPreferences =
        context!!.getSharedPreferences(CUSTOM_PREF_NAME, Context.MODE_PRIVATE)

    override fun setDarkTheme() {
        val editor = sharedPreference.edit()
        editor.putBoolean(themeKey, true)
        editor.apply()
    }

    override fun setLightTheme() {
        val editor = sharedPreference.edit()
        editor.putBoolean(themeKey, false)
        editor.apply()
    }

    override fun loadTheme(): Boolean {
        return sharedPreference.getBoolean(themeKey, false)
    }

    override fun getSelectedMonth(): LocalDate {
        return LocalDate.parse(sharedPreference.getString(monthKey, LocalDate.now().toString()))
    }

    override fun setSelectedMonth(selectedDate: LocalDate) {
        val editor = sharedPreference.edit()
        editor.putString(monthKey, selectedDate.toString())
        editor.apply()
    }

    override fun setSelectedDate(selectedDate: LocalDate) {
        val editor = sharedPreference.edit()
        editor.putString(selectedDateKey, selectedDate.toString())
        editor.apply()
    }

    override fun getSelectedDate(): LocalDate {
        return LocalDate.parse(sharedPreference.getString(selectedDateKey, LocalDate.now().toString()))
    }

    override fun setLanguage(language: String) {
        val editor = sharedPreference.edit()
        editor.putString(monthKey, language)
        editor.apply()
    }

    override fun getLanguage(): String {
        return sharedPreference.getString(languageKey, "English")!!
    }

    override fun setUserTheme(theme: String) {
        val editor = sharedPreference.edit()
        editor.putString(userTheme, theme)
        editor.apply()
    }

    override fun getUserTheme(): String {
        val defaultTheme = "Theme.Main"
        return sharedPreference.getString(userTheme, defaultTheme)!!
    }
}