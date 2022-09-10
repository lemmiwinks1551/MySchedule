package com.example.projectnailsschedule.ui.dataShort

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.example.projectnailsschedule.dateStatusDB.DateStatusDbHelper

/** Получает данные из БД за выбранную дату
 * Хранит полученные данные в словаре */

class DateShorGetDbData {

    private lateinit var context: Context
    val dateStatusDbHelper = DateStatusDbHelper(context)
    var dbStatus: SQLiteDatabase? = null
    var cursor: Cursor? = null



}