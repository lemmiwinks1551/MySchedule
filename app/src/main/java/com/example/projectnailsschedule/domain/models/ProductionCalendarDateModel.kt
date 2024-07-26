package com.example.projectnailsschedule.domain.models

data class ProductionCalendarDateModel(
    val id: Int,
    val date: String,
    val typeId: Int,
    val typeText: String,
    val note: String,
    val weekDay: String
)