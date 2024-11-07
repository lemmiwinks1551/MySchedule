package com.example.projectnailsschedule.data.storage

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.projectnailsschedule.domain.models.dto.AppointmentDto
import com.example.projectnailsschedule.domain.repository.dao.ScheduleRemoteDbDao

@Database(
    entities = [AppointmentDto::class],
    version = 1
)
abstract class ScheduleSyncDb : RoomDatabase() {
    abstract fun getDao(): ScheduleRemoteDbDao

    companion object {
        fun getDb(context: Context): ScheduleSyncDb {
            return Room.databaseBuilder(
                context.applicationContext,
                ScheduleSyncDb::class.java,
                "ScheduleSyncDb.db"
            ).build()
        }
    }
}