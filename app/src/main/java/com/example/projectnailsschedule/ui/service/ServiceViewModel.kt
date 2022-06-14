package com.example.projectnailsschedule.ui.service

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ServiceViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Здесь будет прайс"
    }
    val text: LiveData<String> = _text
}