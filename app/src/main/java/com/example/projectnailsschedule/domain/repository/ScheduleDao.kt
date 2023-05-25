package com.example.projectnailsschedule.domain.repository

import androidx.room.*
import com.example.projectnailsschedule.domain.models.AppointmentModelDb
import kotlinx.coroutines.flow.Flow

@Dao
interface ScheduleDao {
    @Insert
    fun insert(appointmentModelDb: AppointmentModelDb)

    @Query("SELECT * FROM schedule")
    fun selectAll(): Flow<List<AppointmentModelDb>>

    @Query("SELECT * FROM schedule")
    fun selectAllList(): List<AppointmentModelDb>

    @Update
    fun update(appointmentModelDb: AppointmentModelDb)

    @Query("SELECT * FROM schedule WHERE date = :date")
    fun getDateAppointments(date: String): Array<AppointmentModelDb>

    @Delete
    fun delete(appointmentModelDb: AppointmentModelDb)

    @Query("SELECT * FROM schedule WHERE " +
            "date LIKE :searchQuery OR " +
            "name LIKE :searchQuery OR " +
            "time LIKE :searchQuery OR " +
            "procedure LIKE :searchQuery OR " +
            "phone LIKE :searchQuery OR " +
            "notes LIKE :searchQuery")
    fun searchDatabase(searchQuery: String): Flow<List<AppointmentModelDb>>
}