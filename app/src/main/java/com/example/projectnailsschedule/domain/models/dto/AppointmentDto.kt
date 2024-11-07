package com.example.projectnailsschedule.domain.models.dto

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ScheduleRemoteDb")
data class AppointmentDto(
    // Общие поля записи

 @PrimaryKey
 val syncUUID: String,

 val localAppointmentId: Long,

 val userName: String? = null,

 val syncTimestamp: String? = null,

 val syncStatus: String? = null,

 val appointmentDate: String? = null,

 val appointmentTime: String? = null,

 val appointmentNotes: String? = null,

    // Поля клиента в записи

 val clientId: String? = null,

 val clientName: String? = null,

 val clientPhone: String? = null,

 val clientTelegram: String? = null,

 val clientInstagram: String? = null,

 val clientVk: String? = null,

 val clientWhatsapp: String? = null,

 val clientNotes: String? = null,

 val clientPhoto: String? = null,

    // Поля процедуры в записи

 val procedureId: String? = null,

 val procedureName: String? = null,

 val procedurePrice: String? = null,

 val procedureNotes: String? = null
)