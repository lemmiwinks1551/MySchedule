package com.example.projectnailsschedule.domain.models

/***
 * Class model for getting and saving
 * information about appointment
 * */

class AppointmentParams(
    val appointmentDate: String,
    val clientName: String,
    val startTime: String,
    val procedureName: String,
    val phoneNum: String,
    val misc: String
) {

}