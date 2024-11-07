package com.example.projectnailsschedule.domain.repository.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.projectnailsschedule.domain.models.AppointmentModelDb
import kotlinx.coroutines.flow.Flow

@Dao
interface ScheduleDao {
    @Insert
    suspend fun insert(appointmentModelDb: AppointmentModelDb): Long

    @Update
    suspend fun update(appointmentModelDb: AppointmentModelDb)

    @Delete
    suspend fun delete(appointmentModelDb: AppointmentModelDb)

    @Query("SELECT * FROM schedule WHERE date = :date ORDER BY time")
    suspend fun getDateAppointments(date: String): MutableList<AppointmentModelDb>

    @Query("SELECT * FROM schedule WHERE date LIKE :dateMonth ORDER BY date")
    suspend fun getMonthAppointments(dateMonth: String): MutableList<AppointmentModelDb>

    @Query("SELECT * FROM schedule")
    fun selectAllAppointmentsLiveData(): Flow<List<AppointmentModelDb>>

    @Query(
        "SELECT * FROM schedule WHERE " +
                "date LIKE :searchQuery OR " +
                "name LIKE :searchQuery OR " +
                "time LIKE :searchQuery OR " +
                "procedure LIKE :searchQuery OR " +
                "phone LIKE :searchQuery OR " +
                "vk LIKE :searchQuery OR " +
                "telegram LIKE :searchQuery OR " +
                "instagram LIKE :searchQuery OR " +
                "whatsapp LIKE :searchQuery OR " +
                "notes LIKE :searchQuery ORDER BY _id DESC"
    )
    fun searchAppointment(searchQuery: String): MutableList<AppointmentModelDb>

    @Query("UPDATE schedule SET " +
            "name = :clientName, phone = :clientPhone, " +
            "vk = :clientVk, telegram = :clientTelegram, " +
            "instagram = :clientInstagram, whatsapp = :clientWhatsapp, " +
            "clientNotes = :clientNotes, photo = :clientPhoto " +
            "WHERE clientId = :clientId")
    suspend fun updateClientInAppointments(
        clientId: Long,
        clientName: String?,
        clientPhone: String?,
        clientVk: String?,
        clientTelegram: String?,
        clientInstagram: String?,
        clientWhatsapp: String?,
        clientNotes: String?,
        clientPhoto: String?,
    )

    @Query("SELECT * FROM schedule")
    suspend fun getAll(): List<AppointmentModelDb>
}