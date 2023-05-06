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

    @Update
    fun update(appointmentModelDb: AppointmentModelDb)

    @Query("SELECT * FROM schedule WHERE date = :date")
    fun getDateAppointments(date: String): Array<AppointmentModelDb>

    @Delete
    fun delete(appointmentModelDb: AppointmentModelDb)
}