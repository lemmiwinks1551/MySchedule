package com.example.projectnailsschedule.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/***
 * Class model for date
 * information about appointment
 * */

@Parcelize
class DateParams(
    val _id: Int? = null,
    val date: String? = null,
    var status: String? = null
) : Parcelable {
}