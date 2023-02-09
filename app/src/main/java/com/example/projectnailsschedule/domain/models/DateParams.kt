package com.example.projectnailsschedule.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.LocalDate

/***
 * Class model for date
 * information about appointment
 * */

@Parcelize
class DateParams(
    val _id: Int? = null,
    var date: LocalDate? = null,
    var status: String? = null,
    var appointmentCount: Int? = null
) : Parcelable {

    override fun toString(): String {
        return String.format("$_id")
    }
}