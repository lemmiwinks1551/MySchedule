package com.example.projectnailsschedule.domain.repository.repo

import com.example.projectnailsschedule.domain.models.dto.AppointmentDto
import java.util.Date

interface ScheduleSyncRepository {

    suspend fun insert(appointmentDto: AppointmentDto): Long

    suspend fun update(appointmentDto: AppointmentDto)

    suspend fun delete(appointmentDto: AppointmentDto)

    suspend fun getAll(): List<AppointmentDto>

    suspend fun getNotSyncAppointments(): List<AppointmentDto>

    suspend fun getDeletedAppointments(): List<AppointmentDto>

    suspend fun getByLocalAppointmentId(localAppointmentId: Long): AppointmentDto?

    suspend fun getMaxAppointmentTimestamp(): Date?
}