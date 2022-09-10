package com.example.projectnailsschedule.ui.dataShort

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.example.projectnailsschedule.dateStatusDB.DateStatusDbHelper

/** Получает данные из БД за выбранную дату
 * Хранит полученные данные в словаре */

class DateShorGetDbData {

    var dateStatusDbHelper = DateStatusDbHelper(context)
    private lateinit var context: Context
    private lateinit var dbDate: SQLiteDatabase
    private lateinit var cursor: Cursor


fun getDataRows(): Int {
    // Возвращает количество строк в курсоре
    return cursor.count
}

}