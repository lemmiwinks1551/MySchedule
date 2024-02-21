package com.example.projectnailsschedule.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.LocalDate

@Parcelize
data class DateParams(
    val _id: Int? = null,
    var date: LocalDate? = null,
    var appointments: Int? = null,
    var appointmentsList: MutableList<AppointmentModelDb>? = null
) : Parcelable {

    override fun toString(): String {
        return String.format(
            "id = $_id " +
                    "date = $date " +
                    "appointmentCount = $appointments, " +
                    "appointmentsArray = ${appointmentsList?.size}"
        )
    }
}