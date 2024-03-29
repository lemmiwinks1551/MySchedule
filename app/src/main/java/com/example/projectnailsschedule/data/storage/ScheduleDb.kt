package com.example.projectnailsschedule.data.storage

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.projectnailsschedule.domain.models.AppointmentModelDb
import com.example.projectnailsschedule.domain.repository.ScheduleDao

@Database(
    entities = [AppointmentModelDb::class],
    version = 2,
    autoMigrations = [AutoMigration(from = 1, to = 2)]
)
abstract class ScheduleDb : RoomDatabase() {
    abstract fun getDao(): ScheduleDao

    companion object {
        fun getDb(context: Context): ScheduleDb {
            return Room.databaseBuilder(
                context.applicationContext,
                ScheduleDb::class.java,
                "schedule.db"
            ).build()
        }
    }
}