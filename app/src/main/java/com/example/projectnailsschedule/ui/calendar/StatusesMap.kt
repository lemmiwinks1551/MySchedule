package com.example.projectnailsschedule.ui.calendar

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.example.projectnailsschedule.service.Converter
import com.example.projectnailsschedule.database.DateStatusDbHelper
import java.time.YearMonth

class StatusesMap : Thread() {

    /**
     * Получает на вход массив дней в месяце,
     * получает из БД статус по каждому дню
     * отдает в главный поток словарь */


    val dayStatuses = mutableMapOf<String, String>()
    private val LOG = this::class.simpleName
    private lateinit var daysInMonthArray: ArrayList<String>
    private lateinit var context: Context
    private lateinit var yearMonth: YearMonth
    private lateinit var year: String
    private lateinit var month: String

    override fun run() {
        // Получаем по каждому дню статус из БД и устанавливаем в словарь
        Log.e(LOG, "Run $name")

        val dateStatusDbHelper = DateStatusDbHelper(context)
        var dbStatus: SQLiteDatabase? = null
        var cursor: Cursor? = null

        month = yearMonth.monthValue.toString()
        month = Converter().addZero(month)

        year = yearMonth.year.toString()

        for (day in daysInMonthArray) {
            // Для каждого дня в массиве получить статус и добавить в словарь
            if (day.isEmpty()) {
                // Если день пустой - перейти к следующему элементу массива
                continue
            }

            var dd = Converter().addZero(day)
            val date = String.format("$dd.$month.$year")
            Log.e(LOG, String.format("Date for queue: $date"))

            dbStatus = dateStatusDbHelper.readableDatabase
            cursor = dateStatusDbHelper.fetchDate(date, dbStatus!!)
            dd = Converter().removeZero(dd)

            if (cursor.moveToFirst()) {
                val columnIndex = cursor.getColumnIndex("status")
                Log.e(LOG, "Day $date, set status ${cursor.getString(columnIndex)}")
                dayStatuses[dd] = cursor.getString(columnIndex)
            } else {
                dayStatuses[dd] = ""
            }
        }
        cursor?.close()
        dbStatus?.close()
    }


    // Сеттеры:

    fun setDaysOfMonth(_daysInMonthArray: ArrayList<String>) {
        // Установить массив дней
        Log.e(LOG, "Array received")
        daysInMonthArray = _daysInMonthArray
    }

    fun setYearMonth(_yearMonth: YearMonth) {
        // Установить объект YearMonth
        Log.e(LOG, "YearMonth received")
        yearMonth = _yearMonth
    }

    fun setContext(_context: Context) {
        // Установить Context
        Log.e(LOG, "Context received")
        context = _context
    }
}