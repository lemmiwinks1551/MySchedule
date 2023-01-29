package com.example.projectnailsschedule.data.repository

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.example.projectnailsschedule.data.storage.CalendarDbHelper
import com.example.projectnailsschedule.domain.models.DateParams
import com.example.projectnailsschedule.domain.repository.CalendarRepository

class CalendarRepositoryImpl(context: Context?) : CalendarRepository {

    val log = this::class.simpleName
    private var calendarDbHelper: CalendarDbHelper = CalendarDbHelper(context)
    private var db: SQLiteDatabase = calendarDbHelper.writableDatabase

    override fun addDate(dateParams: DateParams): Boolean {
        calendarDbHelper.addDate(dateParams = dateParams, db = db)
        db.close()
        return true
    }

    override fun getDate(dateParams: DateParams): DateParams {
        val db: SQLiteDatabase = calendarDbHelper.writableDatabase
        val cursor: Cursor = calendarDbHelper.getDate(dateParams, db)
        calendarDbHelper.getDate(dateParams = dateParams, db = db)

        if (cursor.moveToFirst()) {
            val columnIndex = cursor.getColumnIndex(CalendarDbHelper.COLUMN_STATUS)
            Log.e(log, "Day ${dateParams.date}, set status ${cursor.getString(columnIndex)}")
            dateParams.status = cursor.getString(columnIndex)
        }
        cursor.close()
        db.close()
        return dateParams
    }

    override fun editDate(dateParams: DateParams): Boolean {
        calendarDbHelper.editDate(dateParams = dateParams, db = db)
        db.close()
        return true
    }

}