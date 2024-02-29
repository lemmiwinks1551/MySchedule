package com.example.projectnailsschedule.domain.models

import java.time.LocalDate

class DateWeekAppModel(
    // model for ListMonthViewFragment
    val date: LocalDate,
    val weekDay: String,
    val appointmentsList: MutableList<AppointmentModelDb>
)