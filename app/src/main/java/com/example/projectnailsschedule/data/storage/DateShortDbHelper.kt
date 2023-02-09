package com.example.projectnailsschedule.data.storage

import android.content.Context
import android.util.Log
import com.example.projectnailsschedule.util.Util
import com.example.projectnailsschedule.data.storage.ScheduleDbHelper
import java.time.LocalDate

/** Получает данные из БД за выбранную дату
 * Хранит полученные данные в массиве */

class DateShortDbHelper(
    private var selectedDate: LocalDate,
    private var context: Context
) {

    private val LOG = this::class.simpleName
    private val timeNameMap = mutableMapOf<String, String>()
    private var rowsCount = 0

    fun fetchDate() {
        // Получаем записи по дню и добавляем в словарь Клиент-Время
        val date = "${Util().addZero(selectedDate.dayOfMonth.toString())}." +
                "${Util().addZero(selectedDate.monthValue.toString())}." +
                selectedDate.year.toString()
        val databaseHelper = ScheduleDbHelper(context)
        val db = databaseHelper.readableDatabase
        val cursor = databaseHelper.fetchNameDate(date, db)

        if (cursor.moveToFirst()) {
            // Если данные были в БД - заполнить словарь
            do {
                val nameColumnIndex = cursor.getColumnIndex(ScheduleDbHelper.COLUMN_NAME)
                val timeColumnIndex = cursor.getColumnIndex(ScheduleDbHelper.COLUMN_START_TIME)
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