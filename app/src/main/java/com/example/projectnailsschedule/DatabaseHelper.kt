package com.example.projectnailsschedule

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class DatabaseHelper(context: Context?) : SQLiteOpenHelper(context, DATABASE_NAME, null, VERSION) {
    companion object {
        private const val DATABASE_NAME = "schedule.db" // название бд
        private const val VERSION = 20 // версия базы данных
        const val TABLE_NAME = "schedule" // название таблицы в бд

        // Названия столбцов
        const val COLUMN_ID = "_id"
        const val COLUMN_DATE = "date"
        const val COLUMN_START = "start"
        const val COLUMN_PROCEDURE = "procedure"
        const val COLUMN_NAME = "name"
        const val COLUMN_PHONE = "phone"
        const val COLUMN_MISC = "misc"
    }

    override fun onCreate(db: SQLiteDatabase) {
        Log.e("Database", "Создаем БД")
        db.execSQL(
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_DATE + " TEXT NOT NULL, " +
                    COLUMN_START + " TEXT NOT NULL, " +
                    COLUMN_PROCEDURE + " TEXT NOT NULL, " +
                    COLUMN_NAME + " TEXT NOT NULL UNIQUE, " +
                    COLUMN_PHONE + " TEXT NOT NULL UNIQUE, " +
                    COLUMN_MISC + " TEXT);"
        )
        Log.e("Database", "БД создана")

        // Тестовая строка создается при обновлении БД
        db.execSQL(
            "INSERT INTO " + TABLE_NAME + " (" +
                    COLUMN_DATE + ", " +
                    COLUMN_START + ", " +
                    COLUMN_PROCEDURE + ", " +
                    COLUMN_NAME + ", " +
                    COLUMN_PHONE + ", " +
                    COLUMN_MISC + ") VALUES ('01.07.2022', '11:00', 'Наращивание', 'Филип Киркоров', '8 800 555 35 35', '@fkirkorov');"
        )

        db.execSQL(
            "INSERT INTO " + TABLE_NAME + " (" +
                    COLUMN_DATE + ", " +
                    COLUMN_START + ", " +
                    COLUMN_PROCEDURE + ", " +
                    COLUMN_NAME + ", " +
                    COLUMN_PHONE + ", " +
                    COLUMN_MISC + ") VALUES ('01.07.2022', '13:00', 'Коррекция', 'Алла Пугачева', '8 801 555 35 35', 'н/д');"
        )

        db.execSQL(
            "INSERT INTO " + TABLE_NAME + " (" +
                    COLUMN_DATE + ", " +
                    COLUMN_START + ", " +
                    COLUMN_PROCEDURE + ", " +
                    COLUMN_NAME + ", " +
                    COLUMN_PHONE + ", " +
                    COLUMN_MISC + ") VALUES ('01.07.2022', '16:00', 'Коррекция', 'Виктор Цой', '8 803 555 35 35', 'ы');"
        )

        Log.e("Database", "Тестовая строка добавлена")

    }


    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        Log.e("Database", "Обновлена")
        // TODO: Добавить логику, чтобы старая БД переписывалась в новую, а не убивалась 
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }
}
