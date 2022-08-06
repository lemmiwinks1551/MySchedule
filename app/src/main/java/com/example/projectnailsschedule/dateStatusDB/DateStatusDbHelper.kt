package com.example.projectnailsschedule.dateStatusDB

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.projectnailsschedule.DatabaseHelper

/**
 * Методы для взаимодействия с БД по статусам дней календаря:
 * создать БД, обновить БД, добавить строку, удалить строку, обновить строку выполнить запрос
 * */

class DateStatusDbHelper(context: Context?) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, VERSION) {
    companion object {
        private const val DATABASE_NAME = "status.db" // название бд
        private const val VERSION = 1 // версия базы данных
        const val TABLE_NAME = "status" // название таблицы в бд

        // Названия столбцов
        const val COLUMN_ID = "_id"
        const val COLUMN_DATE = "date"
        const val COLUMN_STATUS = "status"

        const val LOG_DATABASE = "StatusDB"

        // Статусы:
        const val STATUS_FREE = "free"
        const val STATUS_MEDIUM = "medium"
        const val STATUS_BUSY = "busy"
        const val STATUS_DAY_OFF = "dayOff"
    }

    fun addDate(date: String, status: String, db: SQLiteDatabase) {
        /** Метод добавляет строку в БД **/
        val query = "INSERT INTO $TABLE_NAME " +
                "($COLUMN_DATE, $COLUMN_STATUS) " +
                "VALUES ('$date', '$status');"
        Log.e(LOG_DATABASE, String.format("Add row query: $query"))
        db.execSQL(query)
        Log.e(LOG_DATABASE, String.format("Add row - success"))
    }

    fun fetchDate(date: String, db: SQLiteDatabase): Cursor {
        // Метод получает строку из БД в завимости от дня
        val query = "SELECT * FROM $TABLE_NAME WHERE $COLUMN_DATE = '$date';"
        Log.e(LOG_DATABASE, String.format("Row № $date fetched"))
        // Получаем данные из бд в виде курсора
        return db.rawQuery(query, null)
    }

    fun updateDate(date: String, status: String, db: SQLiteDatabase) {
        // Метод редактирует выбранную строку
        val query = "UPDATE $TABLE_NAME SET " +
                "$COLUMN_STATUS = '$status' " +
                "WHERE $COLUMN_DATE = '$date';"
        Log.e(LOG_DATABASE, String.format("Edit row query: $query"))
        db.execSQL(query)
        Log.e(LOG_DATABASE, String.format("Edit row success"))
    }

    override fun onCreate(db: SQLiteDatabase) {
        Log.e(LOG_DATABASE, "Status DB creating")
        db.execSQL(
            "CREATE TABLE $TABLE_NAME ($COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "$COLUMN_DATE TEXT NOT NULL," +
                    "$COLUMN_STATUS TEXT NOT NULL);"
        )
        Log.e(LOG_DATABASE, "Status DB created")

        db.execSQL(
            "INSERT INTO $TABLE_NAME (" +
                    "$COLUMN_DATE, " +
                    "$COLUMN_STATUS) VALUES " +
                    "('01.07.2022', 'free');"
        )

        db.execSQL(
            "INSERT INTO $TABLE_NAME (" +
                    "$COLUMN_DATE, " +
                    "$COLUMN_STATUS) VALUES " +
                    "('02.07.2022', 'busy');"
        )

        db.execSQL(
            "INSERT INTO $TABLE_NAME (" +
                    "$COLUMN_DATE, " +
                    "$COLUMN_STATUS) VALUES " +
                    "('03.07.2022', 'medium');"
        )

        db.execSQL(
            "INSERT INTO $TABLE_NAME (" +
                    "$COLUMN_DATE, " +
                    "$COLUMN_STATUS) VALUES " +
                    "('04.07.2022', 'dayOff');"
        )

        Log.e(DatabaseHelper.LOG_DATABASE, "Test row added")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        Log.e(LOG_DATABASE, "Status DB updated")
        // TODO: Добавить логику, чтобы старая БД переписывалась в новую, а не убивалась
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

}