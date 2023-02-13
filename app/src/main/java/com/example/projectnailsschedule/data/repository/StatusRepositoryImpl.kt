package com.example.projectnailsschedule.data.repository

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.example.projectnailsschedule.data.storage.StatusDbHelper
import com.example.projectnailsschedule.domain.models.DateParams
import com.example.projectnailsschedule.domain.repository.StatusRepository

class StatusRepositoryImpl(context: Context?) : StatusRepository {

    val log = this::class.simpleName
    private var calendarDbHelper: StatusDbHelper = StatusDbHelper(context)
    private var db: SQLiteDatabase = calendarDbHelper.writableDatabase

    override fun addDate(dateParams: DateParams): Boolean {
        calendarDbHelper.addDate(dateParams = dateParams, db = db)
        db.close()
        return true
    }

    override fun getStatus(dateParams: DateParams): DateParams {
        val db: SQLiteDatabase = calendarDbHelper.writableDatabase
        val cursor: Cursor = calendarDbHelper.getDate(dateParams, db)
        calendarDbHelper.getDate(dateParams = dateParams, db = db)

        if (cursor.moveToFirst()) {
            val columnIndex = cursor.getColumnIndex(StatusDbHelper.COLUMN_STATUS)
            Log.e(log, "Day ${dateParams.date}, status ${cursor.getString(columnIndex)}")
            dateParams.status = cursor.getString(columnIndex)
        }
        cursor.close()
        db.close()
        return dateParams
    }

    override fun setStatus(dateParams: DateParams): Boolean {
        calendarDbHelper.setStatus(dateParams = dateParams, db = db)
        db.close()
        return true
    }

}