package com.example.projectnailsschedule.presentation.price

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PriceViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Скоро здесь будет много интересного!"
    }
    val text: LiveData<String> = _text
}