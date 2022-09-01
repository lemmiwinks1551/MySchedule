package com.example.projectnailsschedule.ui.calendar

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.example.projectnailsschedule.dateStatusDB.DateStatusDbHelper
import java.time.YearMonth

class StatusesMap : Thread() {

    /**
     * Получает на вход массив дней в месяце,
     * получает из БД статус по каждому дню
     * отдает в главный поток словарь */


    val dayStatuses = mutableMapOf<String, String>()
    private val LOG = "Thread"
    private var daysInMonthArray = ArrayList<String>()
    private var context: Context? = null
    private var yearMonth: YearMonth? = null
    private var year = ""
    private var month = ""

    override fun run() {
        // Получаем по каждому дню статус из БД и устанавливаем в словарь
        Log.e(LOG, "Run $name")

        val dateStatusDbHelper = DateStatusDbHelper(context)
        var dbStatus: SQLiteDatabase? = null
        var cursor: Cursor? = null

        // Дописывает 0 к месяцам из одной цифры
        if (yearMonth?.monthValue.toString().length == 1) {
            month = "0${yearMonth?.monthValue}"
        } else {
            month = yearMonth?.monthValue.toString()
        }

        year = yearMonth?.year.toString()

        for (day in daysInMonthArray) {
            if (day == "") {
                continue
            }

            var dd = if (day.length == 1) "0$day" else day
            val date = String.format("$dd.$month.$year")
            Log.e(LOG, String.format("Date for queue: $date"))

            dbStatus = dateStatusDbHelper.readableDatabase
            cursor = dateStatusDbHelper.fetchDate(date, dbStatus!!)
            dd = if (dd[0].toString() == "0") dd.replace("0", "") else dd

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

    fun setDaysOfMonth(_daysInMonthArray: ArrayList<String>) {
        // Получаем массив дней
        Log.e(LOG, "Array received")
        daysInMonthArray = _daysInMonthArray
    }

    fun setYearMonth(_yearMonth: YearMonth) {
        Log.e(LOG, "YearMonth received")
        yearMonth = _yearMonth
    }

    fun setContext(_context: Context) {
        Log.e(LOG, "Context received")
        context = _context
    }

    override fun interrupt() {
        Log.e(LOG, "Interrupted")
        super.interrupt()
    }

}