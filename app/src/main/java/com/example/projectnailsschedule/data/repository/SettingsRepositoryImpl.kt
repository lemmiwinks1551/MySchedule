package com.example.projectnailsschedule.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.example.projectnailsschedule.domain.repository.SettingsRepository

class SettingsRepositoryImpl(context: Context?) : SettingsRepository {

    private val CUSTOM_PREF_NAME = "Settings"
    private val theme = "theme"
    private val sharedPreference: SharedPreferences =
        context!!.getSharedPreferences(CUSTOM_PREF_NAME, Context.MODE_PRIVATE)

    override fun setDarkTheme() {
        val editor = sharedPreference.edit()
        editor.putBoolean(theme, true)
        editor.apply()
    }

    override fun setLightTheme() {
        val editor = sharedPreference.edit()
        editor.putBoolean(theme, false)
        editor.apply()
    }

    override fun loadTheme(): Boolean {
        return sharedPreference.getBoolean(theme, false)
    }
}