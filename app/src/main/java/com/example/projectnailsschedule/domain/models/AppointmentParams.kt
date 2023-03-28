package com.example.projectnailsschedule.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.LocalDate

/***
 * Class model for getting and saving
 * information about appointment
 * */

@Parcelize
class AppointmentParams(
    val _id: Int? = null, // optional field, only need for editAppointment method
    val appointmentDate: LocalDate? = null,
    val clientName: String? = null,
    val startTime: String? = null,
    val procedure: String? = null,
    val phoneNum: String? = null,
    val misc: String? = null,
    val deleted: Int
) : Parcelable