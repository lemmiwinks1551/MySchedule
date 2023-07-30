package com.example.projectnailsschedule.domain.models

import java.time.LocalDate

class DateWeekAppModel(
    val date: LocalDate,
    val weekDay: String,
    val appointmentsList: MutableList<AppointmentModelDb>
) {
}