package com.example.projectnailsschedule.presentation.about

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AboutViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Скоро здесь будет много интересного!"
    }
    val text: LiveData<String> = _text

    fun sendDevMail() {
        // TODO: not yet implemented 
    }
}