package com.example.projectnailsschedule.domain.models

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.time.LocalDate
import java.time.LocalTime

/***
 * Class model for
 * information about appointment
 * */

@Parcelize
@Entity(tableName = "schedule")
data class AppointmentModelDb(
    @PrimaryKey(autoGenerate = true)
    var _id: Long? = null,

    @ColumnInfo(name = "date")
    val date: String? = null,

    @ColumnInfo(name = "clientId")
    val clientId: Long? = null,

    @ColumnInfo(name = "name")
    val name: String? = null,

    @ColumnInfo(name = "time")
    val time: String? = null,

    @ColumnInfo(name = "procedure")
    val procedure: String? = null,

    @ColumnInfo(name = "phone")
    val phone: String? = null,

    @ColumnInfo(name = "telegram")
    val telegram: String? = null,

    @ColumnInfo(name = "instagram")
    val instagram: String? = null,

    @ColumnInfo(name = "vk")
    val vk: String? = null,

    @ColumnInfo(name = "whatsapp")
    val whatsapp: String? = null,

    @ColumnInfo(name = "notes")
    val notes: String? = null,

    @ColumnInfo(name = "photo")
    val photo: String? = null,

    @ColumnInfo(name = "deleted")
    val deleted: Boolean
) : Parcelable {

    override fun toString(): String {
        return "Appointment (" +
                "id = ${this._id}, date = ${this.date}, " +
                "name = ${this.name}, time = ${this.time}, " +
                "procedure = ${this.procedure}, phone = ${this.phone}, " +
                "telegramContact = ${this.telegram}, " +
                "notes = ${this.notes}, deleted = ${this.deleted})"
    }
}
