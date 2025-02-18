package com.example.projectnailsschedule.data.storage

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.projectnailsschedule.domain.models.CalendarDateModelDb
import com.example.projectnailsschedule.domain.repository.dao.CalendarDao

@Database(
    entities = [CalendarDateModelDb::class],
    version = 2,
    autoMigrations = [
        AutoMigration(from = 1, to = 2)
    ]
)
abstract class CalendarDb : RoomDatabase() {
    abstract fun getDao(): CalendarDao

    companion object {
        fun getDb(context: Context): CalendarDb {
            return Room.databaseBuilder(
                context.applicationContext,
                CalendarDb::class.java,
                "calendar.db"
            ).build()
        }
    }
}
