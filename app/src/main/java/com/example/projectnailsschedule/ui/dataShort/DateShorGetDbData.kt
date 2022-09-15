package com.example.projectnailsschedule.ui.dataShort

import android.content.Context
import android.database.Cursor
import android.util.Log
import com.example.projectnailsschedule.Converter
import com.example.projectnailsschedule.DatabaseHelper

/** Получает данные из БД за выбранную дату
 * Хранит полученные данные в массиве */

class DateShorGetDbData(
    private var day: String,
    private var month: String,
    private var year: String,
    private var context: Context
) {

    private val LOG = "DateShort Thread"
    private val timeNameMap = mutableMapOf<String, String>()

    private var cursor: Cursor? = null
    private var rowsCount = 0


    fun fetchDate() {

        // Получаем записи по дню и добавляем в словарь Клиент-Время
        val date = "${Converter().addZero(day)}.${Converter().addZero(month)}.$year"
        val databaseHelper = DatabaseHelper(context)
        val db = databaseHelper.readableDatabase
        val cursor = databaseHelper.fetchNameDate(date, db)

        if (cursor.moveToFirst()) {
            // Если данные были в БД - заполнить словарь
            do {
                val nameColumnIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_NAME)
                val timeColumnIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_START)
                timeNameMap[cursor.getString(nameColumnIndex)] = cursor.getString(timeColumnIndex)
            } while (cursor.moveToNext())
        } else {
            Log.e(LOG, "No data in cursor")
        }
        rowsCount = cursor.count
        cursor.close()
        db.close()
    }

    fun getDataRows(): Int {
        // Возвращает количество строк в курсоре
        return rowsCount
    }

    fun getTimeNameMap(): MutableMap<String, String> {
        // Возвращает словарь
        return timeNameMap
    }

}