package com.example.projectnailsschedule.data.storage

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.projectnailsschedule.domain.models.ClientModelDb
import com.example.projectnailsschedule.domain.repository.ClientsDao

@Database(entities = [ClientModelDb::class],
    version = 3,
    autoMigrations = [AutoMigration(from = 2, to = 3)]
)
abstract class ClientsDb : RoomDatabase() {
    abstract fun getDao(): ClientsDao

    companion object {
        fun getDb(context: Context): ClientsDb {
            return Room.databaseBuilder(
                context.applicationContext,
                ClientsDb::class.java,
                "clients.db"
            ).build()
        }
    }
}