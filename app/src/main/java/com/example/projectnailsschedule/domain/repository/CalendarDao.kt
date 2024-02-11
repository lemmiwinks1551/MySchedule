package com.example.projectnailsschedule.domain.repository

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.projectnailsschedule.domain.models.CalendarDateModelDb

@Dao
interface CalendarDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(calendarDateModelDb: CalendarDateModelDb)

    @Update
    suspend fun update(calendarDateModelDb: CalendarDateModelDb)

    @Delete
    suspend fun delete(calendarDateModelDb: CalendarDateModelDb)

    @Query("SELECT * FROM calendar WHERE date = :date")
    suspend fun selectDate(date: String) : CalendarDateModelDb
}