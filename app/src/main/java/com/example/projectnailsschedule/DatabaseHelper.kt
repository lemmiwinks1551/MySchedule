package com.example.projectnailsschedule

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class DatabaseHelper(context: Context?) : SQLiteOpenHelper(context, DATABASE_NAME, null, VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        Log.e("Database", "Создана")

        db.execSQL(
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_START + " TEXT NOT NULL, " +
                    COLUMN_END + " TEXT NOT NULL, " +
                    COLUMN_NAME + " TEXT NOT NULL UNIQUE, " +
                    COLUMN_PHONE + " TEXT NOT NULL UNIQUE, " +
                    COLUMN_MISC + " TEXT);"
        )

        db.execSQL(
            "INSERT INTO " + TABLE_NAME + " (" +
                    COLUMN_START + ", " +
                    COLUMN_END + ", " +
                    COLUMN_NAME + "," +
                    COLUMN_PHONE + "," +
                    COLUMN_MISC + ") VALUES ('11:00', '13:00', 'Имя', '8 800 555 35 35', 'instagram');"
        )
    }
    companion object {
        private const val DATABASE_NAME = "schedule.db" // название бд
        private const val VERSION = 2 // версия базы данных
        const val TABLE_NAME = "schedule" // название таблицы в бд

        // Названия столбцов
        const val COLUMN_ID = "_id"
        const val COLUMN_START = "start"
        const val COLUMN_END = "end"
        const val COLUMN_NAME = "name"
        const val COLUMN_PHONE = "phone"
        const val COLUMN_MISC = "misc"
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // TODO: Добавить логику, чтобы старая БД переписывалась в новую, а не убивалась 
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }
}
