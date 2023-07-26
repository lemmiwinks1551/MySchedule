package com.example.projectnailsschedule.domain.models

class DateWeekAppModel(
    val day: String,
    val weekDay: String,
    val appointmentsList: Array<AppointmentModelDb>
) {
}