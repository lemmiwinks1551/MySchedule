package com.example.projectnailsschedule.data.storage

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.projectnailsschedule.domain.models.ProcedureModelDb
import com.example.projectnailsschedule.domain.repository.ProcedureDao

@Database(
    entities = [ProcedureModelDb::class],
    version = 1
)
abstract class ProceduresDb: RoomDatabase() {
    abstract fun getDao(): ProcedureDao

    companion object {
        fun getDb(context: Context): ProceduresDb {
            return Room.databaseBuilder(
                context.applicationContext,
                ProceduresDb::class.java,
                "procedures.db"
            ).build()
        }
    }
}