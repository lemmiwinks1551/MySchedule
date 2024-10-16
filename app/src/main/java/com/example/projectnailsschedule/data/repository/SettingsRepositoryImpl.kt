package com.example.projectnailsschedule.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.example.projectnailsschedule.domain.repository.repo.SettingsRepository

class SettingsRepositoryImpl(context: Context?) : SettingsRepository {

    private val CUSTOM_PREF_NAME = "Settings"
    private val themeKey = "theme"
    private val monthKey = "month"
    private val languageKey = "language"
    private val userTheme = "theme"
    private val jwtKey = "jwt"

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

    override fun setJwt(jwt: String?): Boolean {
        val editor = sharedPreference.edit()
        editor.putBoolean(jwtKey, true)
        editor.apply()
        return true
    }

    override fun getJwt(): String? {
        return sharedPreference.getString(jwtKey, null)
    }
}