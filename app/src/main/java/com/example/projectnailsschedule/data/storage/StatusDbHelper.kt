package com.example.projectnailsschedule.data.storage

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.projectnailsschedule.domain.models.DateParams
import com.example.projectnailsschedule.util.WorkFolders

/**
 * Методы для взаимодействия с БД по статусам дней календаря:
 * создать БД, обновить БД, добавить строку, удалить строку, обновить строку выполнить запрос
 * https://developer.android.com/training/data-storage
 * */

class StatusDbHelper(context: Context?) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, VERSION) {

    companion object {

        // Получить название БД (если создана спец. папка. то в неё положить файл БД, если нет - внутри приложения
        var DATABASE_NAME = String.format("${WorkFolders().getFolderPath()}/status.db") // название БД>
        private const val VERSION = 2       // версия базы данных
        var TABLE_NAME = "status"           // название таблицы в бд

        // Названия столбцов
        const val COLUMN_ID = "_id"
        const val COLUMN_DATE = "date"
        const val COLUMN_STATUS = "status"

        val log = this::class.simpleName

        // Статусы:
        const val STATUS_FREE = "free"
        const val STATUS_MEDIUM = "medium"
        const val STATUS_BUSY = "busy"
        const val STATUS_DAY_OFF = "dayOff"
    }

    fun addDate(dateParams: DateParams, db: SQLiteDatabase) {
        /** Метод добавляет строку в БД **/
        val query = "INSERT INTO $TABLE_NAME " +
                "($COLUMN_DATE, $COLUMN_STATUS) " +
                "VALUES ('${dateParams.date}', '${dateParams.status}');"
        Log.e(log, String.format("Add row query: $query"))
        db.execSQL(query)
        Log.e(log, String.format("Add row - success"))
    }

    fun getDate(dateParams: DateParams, db: SQLiteDatabase): Cursor {
        // Метод получает строку из БД в завимости от дня
        val query = "SELECT * FROM $TABLE_NAME WHERE $COLUMN_DATE = '${dateParams.date}';"
        Log.e(log, String.format("Row № ${dateParams.date} fetched"))
        // Получаем данные из бд в виде курсора
        return db.rawQuery(query, null)
    }

    fun setStatus(dateParams: DateParams, db: SQLiteDatabase) {
        // Set status in date
        val query = "UPDATE $TABLE_NAME SET " +
                "$COLUMN_STATUS = '${dateParams.status}' " +
                "WHERE $COLUMN_DATE = '${dateParams.date}';"
        Log.e(log, String.format("Edit row query: $query"))
        db.execSQL(query)
        Log.e(log, String.format("Edit row success"))
    }

    override fun onCreate(db: SQLiteDatabase) {
        Log.e(log, "Status DB creating")
        db.execSQL(
            "CREATE TABLE $TABLE_NAME ($COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "$COLUMN_DATE TEXT NOT NULL," +
                    "$COLUMN_STATUS TEXT NOT NULL);"
        )
        Log.e(log, "Status DB created")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        Log.e(log, "Status DB updated")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

}