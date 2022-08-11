package com.example.projectnailsschedule.ui.clients

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ClientsViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Здесь будет клиентская база"
    }
    val text: LiveData<String> = _text
}