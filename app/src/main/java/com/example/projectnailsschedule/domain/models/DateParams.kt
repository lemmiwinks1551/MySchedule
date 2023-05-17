package com.example.projectnailsschedule.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.LocalDate

/***
 * Class model for
 * information about date appointment
 * */

@Parcelize
data class DateParams(
    val _id: Int? = null,
    var date: LocalDate? = null,
    var appointmentCount: Int? = null
) : Parcelable {

    override fun toString(): String {
        return String.format("id = $_id date = $date appointmentCount = $appointmentCount")
    }
}