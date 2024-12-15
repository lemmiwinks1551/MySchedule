package com.example.projectnailsschedule.domain.repository.dao

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

    @Query("SELECT * FROM calendar WHERE date = :date AND (syncStatus != 'DELETED' OR syncStatus IS NULL)")
    suspend fun selectDate(date: String) : CalendarDateModelDb

    @Query("SELECT * FROM calendar")
    suspend fun getAll(): List<CalendarDateModelDb>

    @Query("SELECT * FROM calendar WHERE syncStatus = :syncStatus")
    suspend fun getNotSync(syncStatus: String = "NotSynchronized"): List<CalendarDateModelDb>

    @Query("SELECT * FROM calendar WHERE syncStatus = :syncStatus")
    suspend fun getDeleted(syncStatus: String = "DELETED"): List<CalendarDateModelDb>

    @Query("SELECT * FROM calendar WHERE _id = :id")
    suspend fun getById(id: Long): CalendarDateModelDb?

    @Query("SELECT MAX(syncTimestamp) FROM calendar")
    suspend fun getMaxTimestamp(): Long?

    @Query("SELECT * FROM calendar WHERE syncUUID = :syncUUID")
    suspend fun getBySyncUUID(syncUUID: String): CalendarDateModelDb?

    @Query("SELECT COUNT(*) FROM calendar")
    suspend fun getCount(): Long
}