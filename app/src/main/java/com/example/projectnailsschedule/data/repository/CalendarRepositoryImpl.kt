package com.example.projectnailsschedule.data.repository

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.example.projectnailsschedule.data.storage.CalendarDbHelper
import com.example.projectnailsschedule.domain.models.DateParams
import com.example.projectnailsschedule.domain.repository.CalendarRepository

class CalendarRepositoryImpl(context: Context?) : CalendarRepository {
    private var calendarDbHelper: CalendarDbHelper = CalendarDbHelper(context)
    private var db: SQLiteDatabase = calendarDbHelper.writableDatabase

    override fun addDate(dateParams: DateParams): Boolean {
        calendarDbHelper.addDate(dateParams = dateParams, db = db)
        db.close()
        return true
    }

    override fun getDate(dateParams: DateParams): Boolean {
        calendarDbHelper.getDate(dateParams = dateParams, db = db)
        db.close()
        // TODO: need to return dateParams 
        return true
    }

    override fun editDate(dateParams: DateParams): Boolean {
        calendarDbHelper.editDate(dateParams = dateParams, db = db)
        db.close()
        return true
    }

}