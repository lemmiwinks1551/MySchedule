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
        private const val VERSION = 4

        // Table name
        const val TABLE_NAME = "settings" // название таблицы в бд

        // Columns
        const val COLUMN_ID = "_id"
        const val COLUMN_SETTING = "setting"
        const val COLUMN_VALUE = "value"

        val LOG = this::class.simpleName
    }

    override fun onCreate(db: SQLiteDatabase) {
        val theme = "theme"
        val light = "light"

        Log.e(LOG, "Creating database")
        db.execSQL(
            "CREATE TABLE $TABLE_NAME (${COLUMN_ID} INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "$COLUMN_SETTING TEXT NOT NULL, " +
                    "$COLUMN_VALUE TEXT NOT NULL);"
        )
        Log.e(LOG, "Database was created")

        /** Insert a default Theme row */
        val query = "INSERT INTO $TABLE_NAME " +
                "($COLUMN_SETTING, $COLUMN_VALUE) " +
                "VALUES ('$theme', '$light');"
        Log.e(LOG, query)
        db.execSQL(query)
        Log.e(LOG, String.format("Theme row added"))
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVercion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
        Log.e(LOG, "Database was updated")
    }

    fun getRow(setting: String, db: SQLiteDatabase): Cursor {
        /** Select a row from a database */
        val query =
            "SELECT * FROM $TABLE_NAME WHERE $COLUMN_SETTING = '$setting';"
        Log.e(LOG, query)
        Log.e(LOG, String.format("Setting $setting got"))
        return db.rawQuery(query, null)
    }

    fun updateRow(setting: String, value: String, db: SQLiteDatabase) {
        /** Update a row */
        Log.e(LOG, "Updating row $setting with value $value")
        val query = "UPDATE $TABLE_NAME SET " +
                "$COLUMN_VALUE = '$value' " +
                "WHERE $COLUMN_SETTING = '$setting';"
        Log.e(ScheduleDbHelper.LOG, String.format("Update row query: $query"))
        db.execSQL(query)
        Log.e(ScheduleDbHelper.LOG, String.format("Row was updated"))
    }
}