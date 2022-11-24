package com.example.projectnailsschedule.database

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.projectnailsschedule.service.WorkFolders

/** Methods for interacting with Settings database */

class SettingsDbHelper(context: Context?) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, VERSION) {
    companion object {
        // Bd name
        var DATABASE_NAME = String.format("${WorkFolders().getFolderPath()}/settings.db")

        // Current bd version
        private const val VERSION = 1

        // Table name
        const val TABLE_NAME = "settings" // название таблицы в бд

        // Columns
        const val COLUMN_ID = "_id"
        const val COLUMN_SETTING = "setting"
        const val COLUMN_VALUE = "value"

        val LOG = this::class.simpleName
    }

    override fun onCreate(p0: SQLiteDatabase?) {
        TODO("Not yet implemented")
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        TODO("Not yet implemented")
    }

    fun getRow(day: String, db: SQLiteDatabase): Cursor {
        /** Select a row from a database */
        val query =
            "SELECT * FROM $TABLE_NAME WHERE ${ScheduleDbHelper.COLUMN_DATE} = '$day' ORDER BY ${ScheduleDbHelper.COLUMN_START_TIME} ASC;"
        Log.e(ScheduleDbHelper.LOG, String.format("Row № $day fetched"))
        return db.rawQuery(query, null)
    }

    fun editId(extraArray: ArrayList<String>, db: SQLiteDatabase) {
        /** Update a row */
        val query = "UPDATE ${ScheduleDbHelper.TABLE_NAME} SET " +
                "${ScheduleDbHelper.COLUMN_DATE} = '${extraArray[1]}', " +
                "${ScheduleDbHelper.COLUMN_START_TIME} = '${extraArray[2]}', " +
                "${ScheduleDbHelper.COLUMN_PROCEDURE} = '${extraArray[3]}', " +
                "${ScheduleDbHelper.COLUMN_NAME} = '${extraArray[4]}', " +
                "${ScheduleDbHelper.COLUMN_PHONE} = '${extraArray[5]}', " +
                "${ScheduleDbHelper.COLUMN_MISC} = '${extraArray[6]}' " +
                "WHERE ${ScheduleDbHelper.COLUMN_ID} = ${extraArray[0]};"
        Log.e(ScheduleDbHelper.LOG, String.format("Edit row query: $query"))
        db.execSQL(query)
        Log.e(ScheduleDbHelper.LOG, String.format("Row was edited successfully"))
    }
}