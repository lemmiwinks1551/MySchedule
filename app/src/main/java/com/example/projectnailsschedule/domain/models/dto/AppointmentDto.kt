package com.example.projectnailsschedule.domain.models.dto

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "ScheduleRemoteDb")
data class AppointmentDto(
    // Общие поля записи

    @PrimaryKey
    val syncUUID: String,

    var localAppointmentId: Long,

    var userName: String? = null,

    var syncTimestamp: Date,

    var syncStatus: String? = null,

    var appointmentDate: String? = null,

    var appointmentTime: String? = null,

    var appointmentNotes: String? = null,

    // Поля клиента в записи

    var clientId: String? = null,

    var clientName: String? = null,

    var clientPhone: String? = null,

    var clientTelegram: String? = null,

    var clientInstagram: String? = null,

    var clientVk: String? = null,

    var clientWhatsapp: String? = null,

    var clientNotes: String? = null,

    var clientPhoto: String? = null,

    // Поля процедуры в записи

    var procedureId: String? = null,

    var procedureName: String? = null,

    var procedurePrice: String? = null,

    var procedureNotes: String? = null
)