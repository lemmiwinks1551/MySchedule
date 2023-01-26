package com.example.projectnailsschedule.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/***
 * Class model for getting and saving
 * information about appointment
 * */

@Parcelize
class AppointmentParams(
    val _id: Int? = null, // optional field, only need for editAppointment method
    val appointmentDate: String? = null,
    val clientName: String? = null,
    val startTime: String? = null,
    val procedureName: String? = null,
    val phoneNum: String? = null,
    val misc: String? = null
) : Parcelable {

}