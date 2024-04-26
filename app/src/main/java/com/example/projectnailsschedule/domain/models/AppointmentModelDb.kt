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
    var clientId: Long? = null,

    @ColumnInfo(name = "name")
    var name: String? = null,

    @ColumnInfo(name = "time")
    val time: String? = null,

    @ColumnInfo(name = "procedure")
    val procedure: String? = null,

    @ColumnInfo(name = "procedureNotes")
    val procedureNotes: String? = null,

    @ColumnInfo(name = "procedurePrice")
    val procedurePrice: String? = null,

    @ColumnInfo(name = "phone")
    var phone: String? = null,

    @ColumnInfo(name = "telegram")
    var telegram: String? = null,

    @ColumnInfo(name = "instagram")
    var instagram: String? = null,

    @ColumnInfo(name = "vk")
    var vk: String? = null,

    @ColumnInfo(name = "whatsapp")
    var whatsapp: String? = null,

    @ColumnInfo(name = "notes")
    var notes: String? = null,

    @ColumnInfo(name = "clientNotes")
    var clientNotes: String? = null,

    @ColumnInfo(name = "photo")
    var photo: String? = null,

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
