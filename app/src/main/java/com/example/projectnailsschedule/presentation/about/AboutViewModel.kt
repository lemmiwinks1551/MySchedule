package com.example.projectnailsschedule.presentation.about

import androidx.lifecycle.ViewModel
import com.example.projectnailsschedule.BuildConfig

class AboutViewModel : ViewModel() {

    val text: String= "Версия ${BuildConfig.VERSION_NAME}"

    fun sendDevMail() {
        // TODO: not yet implemented 
    }
}