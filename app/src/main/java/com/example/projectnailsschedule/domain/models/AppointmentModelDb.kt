package com.example.projectnailsschedule.domain.models

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.time.LocalDate
import java.time.LocalTime

/***
 * Class model for getting and saving
 * information about appointment
 * */

@Parcelize
@Entity(tableName = "schedule")
data class AppointmentModelDb(
    @PrimaryKey(autoGenerate = true)
    val _id: Int? = null,

    @ColumnInfo(name = "date")
    val date: String? = null,

    @ColumnInfo(name = "name")
    val name: String? = null,

    @ColumnInfo(name = "time")
    val time: String? = null,

    @ColumnInfo(name = "procedure")
    val procedure: String? = null,

    @ColumnInfo(name = "phone")
    val phone: String? = null,

    @ColumnInfo(name = "notes")
    val notes: String? = null,

    @ColumnInfo(name = "deleted")
    val deleted: Boolean
) : Parcelable {

    override fun toString(): String {
        return "Appointment (" +
                "id = ${this._id}, date = ${this.date}, " +
                "name = ${this.name}, time = ${this.time}, " +
                "procedure = ${this.procedure}, phone = ${this.phone}, " +
                "notes = ${this.notes}, deleted = ${this.deleted})"
    }
}
