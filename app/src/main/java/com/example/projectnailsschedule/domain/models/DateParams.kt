package com.example.projectnailsschedule.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.LocalDate

@Parcelize
data class DateParams(
    var date: LocalDate? = null,
    var appointmentsList: MutableList<AppointmentModelDb>? = null
) : Parcelable {

    override fun toString(): String {
        return String.format(
            "date = $date " +
                    "appointmentsArray = ${appointmentsList?.size}"
        )
    }
}