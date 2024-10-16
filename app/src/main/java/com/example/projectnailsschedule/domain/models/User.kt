package com.example.projectnailsschedule.domain.models

data class User(
    val login: String,      // Автоматический геттер (без сеттера, т.к. val)
    var email: String       // Автоматические геттер и сеттер
)
