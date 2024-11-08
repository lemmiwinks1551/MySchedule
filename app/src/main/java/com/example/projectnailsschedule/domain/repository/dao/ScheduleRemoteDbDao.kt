package com.example.projectnailsschedule.domain.repository.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.projectnailsschedule.domain.models.dto.AppointmentDto

@Dao
interface ScheduleRemoteDbDao {
    @Insert
    suspend fun insert(appointmentDto: AppointmentDto): Long

    @Update
    suspend fun update(appointmentDto: AppointmentDto)

    @Delete
    suspend fun delete(appointmentDto: AppointmentDto)

    @Query("SELECT * FROM ScheduleRemoteDb")
    suspend fun getAll(): List<AppointmentDto>

    @Query("SELECT * FROM ScheduleRemoteDb WHERE syncStatus = :syncStatus")
    suspend fun getNotSyncAppointments(syncStatus: String = "NotSynchronized"): List<AppointmentDto>

    @Query("SELECT * FROM ScheduleRemoteDb WHERE syncStatus = :syncStatus")
    suspend fun getDeletedAppointments(syncStatus: String = "DELETED"): List<AppointmentDto>

    @Query("SELECT * FROM ScheduleRemoteDb WHERE localAppointmentId = :localAppointmentId")
    suspend fun getByLocalAppointmentId(localAppointmentId: Long): AppointmentDto
}