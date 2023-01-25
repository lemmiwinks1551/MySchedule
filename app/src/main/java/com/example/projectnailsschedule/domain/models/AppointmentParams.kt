package com.example.projectnailsschedule.domain.models

/***
 * Class model for getting and saving
 * information about appointment
 * */

class AppointmentParams(
    val date: String,
    val clientName: String,
    val startTime: String,
    val procedureName: String,
    val phoneNum: String,
    val misc: String
) {

}