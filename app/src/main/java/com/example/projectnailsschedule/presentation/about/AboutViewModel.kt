package com.example.projectnailsschedule.presentation.about

import androidx.lifecycle.ViewModel
import com.example.projectnailsschedule.BuildConfig
import com.example.projectnailsschedule.util.Util

class AboutViewModel : ViewModel() {

    val text: String= "Версия ${BuildConfig.VERSION_NAME}"

}