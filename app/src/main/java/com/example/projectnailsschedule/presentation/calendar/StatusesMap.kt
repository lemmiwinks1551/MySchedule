package com.example.projectnailsschedule.presentation.calendar

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.example.projectnailsschedule.util.Service
import com.example.projectnailsschedule.data.storage.CalendarDbHelper
import com.example.projectnailsschedule.domain.models.DateParams
import java.time.YearMonth
import java.util.*
import kotlin.collections.ArrayList

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

        val calendarDbHelper = CalendarDbHelper(context)
        var dbStatus: SQLiteDatabase? = null
        var cursor: Cursor? = null

        month = yearMonth.monthValue.toString()
        month = Service().addZero(month)

        year = yearMonth.year.toString()

        for (day in daysInMonthArray) {
            // Для каждого дня в массиве получить статус и добавить в словарь
            if (day.isEmpty()) {
                // Если день пустой - перейти к следующему элементу массива
                continue
            }

            var dd = Service().addZero(day)
            val date = String.format("$dd.$month.$year")
            Log.e(LOG, String.format("Date for queue: $date"))

            dbStatus = calendarDbHelper.readableDatabase

            val dateParameterName = DateParams(_id = null, date = date, status = null)
            cursor = calendarDbHelper.getDate(dateParameterName, dbStatus!!)
            dd = Service().removeZero(dd)

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