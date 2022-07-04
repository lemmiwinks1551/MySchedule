package com.example.projectnailsschedule

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

/**
 * Методы для взаимодействия с БД:
 * создать БД, обновить БД, добавить строку, удалить строку, выполнить запрос
 * */

class DatabaseHelper(context: Context?) : SQLiteOpenHelper(context, DATABASE_NAME, null, VERSION) {
    companion object {
        private const val DATABASE_NAME = "schedule.db" // название бд
        private const val VERSION = 23 // версия базы данных
        const val TABLE_NAME = "schedule" // название таблицы в бд

        // Названия столбцов
        const val COLUMN_ID = "_id"
        const val COLUMN_DATE = "date"
        const val COLUMN_START = "start"
        const val COLUMN_PROCEDURE = "procedure"
        const val COLUMN_NAME = "name"
        const val COLUMN_PHONE = "phone"
        const val COLUMN_MISC = "misc"
        const val LOG_DATABASE = "Database"
    }

    fun addRow(
        date: String,
        time: String,
        procedure: String,
        name: String,
        phone: String,
        misc: String,
        db: SQLiteDatabase
    ) {
        // Метод добавляет строку в БД
        db.execSQL(
            "INSERT INTO $TABLE_NAME " +
                    "($COLUMN_DATE, $COLUMN_START, " +
                    "$COLUMN_PROCEDURE, $COLUMN_NAME, " +
                    "$COLUMN_PHONE, $COLUMN_MISC) " +
                    "VALUES " +
                    "('$date', '$time', '$procedure', '$name', '$phone', '$misc');"
        )
        Log.e(LOG_DATABASE, String.format("Row added"))
    }

    fun deleteRow(
        currentId: Int,
        db: SQLiteDatabase
    ) {
        // Метод удаляет строку из БЖ
        Log.e(LOG_DATABASE, String.format("Row № $currentId deleted"))
        db.execSQL("DELETE FROM $TABLE_NAME WHERE $COLUMN_ID = $currentId;")
    }

    fun fetchRow(day: String, db: SQLiteDatabase): Cursor {
        // Метод получает строку из БД в завимости от дня
        val query = "SELECT * FROM $TABLE_NAME WHERE $COLUMN_DATE = '$day' ORDER BY $COLUMN_START ASC;"

        // Получаем данные из бд в виде курсора
        return db.rawQuery(query, null)
    }

    override fun onCreate(db: SQLiteDatabase) {
        Log.e(LOG_DATABASE, "Создаем БД")
        db.execSQL(
            "CREATE TABLE $TABLE_NAME ($COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "$COLUMN_DATE TEXT NOT NULL, " +
                    "$COLUMN_START TEXT NOT NULL, " +
                    "$COLUMN_PROCEDURE TEXT NOT NULL, " +
                    "$COLUMN_NAME TEXT NOT NULL, " +
                    "$COLUMN_PHONE TEXT NOT NULL, " +
                    "$COLUMN_MISC TEXT);"
        )
        Log.e(LOG_DATABASE, "БД создана")

        // Тестовая строка создается при обновлении БД
        db.execSQL(
            "INSERT INTO $TABLE_NAME (" +
                    "$COLUMN_DATE, " +
                    "$COLUMN_START, " +
                    "$COLUMN_PROCEDURE, " +
                    "$COLUMN_NAME, " +
                    "$COLUMN_PHONE," +
                    "$COLUMN_MISC) VALUES " +
                    "('01.07.2022', '00:00', 'Наращивание', 'Имя Фамилия', '8 800 123 45 67', '@test');"
        )
        Log.e(LOG_DATABASE, "Тестовая строка добавлена")

    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        Log.e("Database", "Обновлена")
        // TODO: Добавить логику, чтобы старая БД переписывалась в новую, а не убивалась
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

}
