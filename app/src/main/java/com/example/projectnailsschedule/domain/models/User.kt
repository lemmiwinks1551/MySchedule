package com.example.projectnailsschedule.domain.models

data class User(
    // Автоматический геттер (без сеттера, т.к. val)
    var username: String,
    var password: String?
)
