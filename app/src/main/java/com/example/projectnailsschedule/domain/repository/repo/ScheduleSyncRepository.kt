package com.example.projectnailsschedule.domain.repository.repo

import com.example.projectnailsschedule.domain.models.dto.AppointmentDto

interface ScheduleSyncRepository {

    suspend fun insert(appointmentDto: AppointmentDto): Long

    suspend fun update(appointmentDto: AppointmentDto)

    suspend fun delete(appointmentDto: AppointmentDto)

    suspend fun getAll(): List<AppointmentDto>

    suspend fun getNotSyncAppointments(): List<AppointmentDto>
}