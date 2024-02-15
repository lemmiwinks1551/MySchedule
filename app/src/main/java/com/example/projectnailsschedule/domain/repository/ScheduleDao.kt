package com.example.projectnailsschedule.domain.repository

import androidx.room.*
import com.example.projectnailsschedule.domain.models.AppointmentModelDb
import kotlinx.coroutines.flow.Flow

@Dao
interface ScheduleDao {
    @Insert
    suspend fun insert(appointmentModelDb: AppointmentModelDb)

    @Update
    suspend fun update(appointmentModelDb: AppointmentModelDb)

    @Delete
    suspend fun delete(appointmentModelDb: AppointmentModelDb)

    @Query("SELECT * FROM schedule WHERE date = :date ORDER BY time")
    suspend fun getDateAppointments(date: String): Array<AppointmentModelDb>

    @Query("SELECT * FROM schedule")
    suspend fun selectAllAppointmentsList(): List<AppointmentModelDb>
    // TODO: можно сократить функцию?

    @Query("SELECT * FROM schedule WHERE date LIKE :dateMonth ORDER BY date")
    suspend fun getMonthAppointments(dateMonth: String): MutableList<AppointmentModelDb>

    @Query("SELECT * FROM schedule")
    fun selectAllAppointmentsLiveData(): Flow<List<AppointmentModelDb>>

    @Query("SELECT * FROM schedule WHERE " +
            "date LIKE :searchQuery OR " +
            "name LIKE :searchQuery OR " +
            "time LIKE :searchQuery OR " +
            "procedure LIKE :searchQuery OR " +
            "phone LIKE :searchQuery OR " +
            "vk LIKE :searchQuery OR " +
            "telegram LIKE :searchQuery OR " +
            "instagram LIKE :searchQuery OR " +
            "whatsapp LIKE :searchQuery OR " +
            "notes LIKE :searchQuery")
    fun searchAppointment(searchQuery: String): Flow<List<AppointmentModelDb>>
}