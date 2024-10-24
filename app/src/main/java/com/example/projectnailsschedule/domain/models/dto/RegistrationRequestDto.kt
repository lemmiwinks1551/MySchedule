package com.example.projectnailsschedule.domain.models.dto

data class RegistrationRequestDto(
    val username: String,
    val email: String,
    val password: String
)