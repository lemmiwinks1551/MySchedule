package com.example.projectnailsschedule.data.storage

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.projectnailsschedule.domain.models.AppointmentParams
import com.example.projectnailsschedule.domain.models.DateParams
import com.example.projectnailsschedule.util.WorkFolders

/** Methods for interacting with Schedule database */

class ScheduleDbHelper(context: Context?) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, VERSION) {

    companion object {
        // Bd name
        var DATABASE_NAME = String.format("${WorkFolders().getFolderPath()}/schedule.db")

        // Current bd version
        private const val VERSION = 26

        // Table name
        const val TABLE_NAME = "schedule" // название таблицы в бд

        // Columns
        const val COLUMN_ID = "_id"
        const val COLUMN_DATE = "date"
        const val COLUMN_START_TIME = "time"
        const val COLUMN_PROCEDURE = "procedure"
        const val COLUMN_NAME = "name"
        const val COLUMN_PHONE = "phone"
        const val COLUMN_MISC = "misc"
        const val COLUMN_DELETED = "deleted"
        val log = this::class.simpleName
    }

    override fun onCreate(db: SQLiteDatabase) {
        Log.e(log, "Creating database")
        db.execSQL(
            "CREATE TABLE $TABLE_NAME ($COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "$COLUMN_DATE TEXT NOT NULL, " +
                    "$COLUMN_START_TIME TEXT NOT NULL, " +
                    "$COLUMN_PROCEDURE TEXT NOT NULL, " +
                    "$COLUMN_NAME TEXT NOT NULL, " +
                    "$COLUMN_PHONE TEXT NOT NULL, " +
                    "$COLUMN_MISC TEXT NOT NULL, " +
                    "$COLUMN_DELETED INTEGER NOT NULL);"
        )
        Log.e(log, "DB created")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        Log.e(log, "Updating database")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
        Log.e(log, "Database was updated")
    }

    fun saveAppointmentBD(appointmentParams: AppointmentParams, db: SQLiteDatabase) {
        /** Insert a new row */
        var query: String?

        with(appointmentParams) {
            query = "INSERT INTO $TABLE_NAME " +
                    "($COLUMN_DATE, " +
                    "$COLUMN_START_TIME, " +
                    "$COLUMN_PROCEDURE, " +
                    "$COLUMN_NAME, " +
                    "$COLUMN_PHONE, " +
                    "$COLUMN_MISC, " +
                    "$COLUMN_DELETED) " +
                    "VALUES (" +
                    "'${appointmentDate}', " +
                    "'${startTime}', " +
                    "'${procedure}', " +
                    "'${clientName}', " +
                    "'${phoneNum}', " +
                    "'${misc}', " +
                    "'${deleted}');"
        }
        Log.e(log, String.format("Add row query: $query"))
        db.execSQL(query)
        Log.e(log, String.format("Add row - success"))
    }

    fun getDateAppointments(dateParams: DateParams, db: SQLiteDatabase): Cursor {
        val query = "SELECT * FROM $TABLE_NAME WHERE $COLUMN_DATE = '${dateParams.date}';"
        Log.e(log, String.format("getDateAppointments query: $query"))
        return db.rawQuery(query, null)
    }

    fun deleteAppointment(currentId: Int, db: SQLiteDatabase) {
        /** Delete a row */
        val query = "DELETE FROM $TABLE_NAME WHERE $COLUMN_ID = $currentId;"
        Log.e(log, String.format("Delete row query: $query"))
        db.execSQL(query)
        Log.e(log, String.format("Delete row - success"))
    }

    fun searchAppointments(db: SQLiteDatabase): Cursor {
        /** Get a row(s) from a database */
        val query = "SELECT * FROM $TABLE_NAME WHERE $COLUMN_DELETED = 0 ORDER BY $COLUMN_DATE DESC"
        Log.e(log, String.format("All datatable fetched"))
        return db.rawQuery(query, null)
    }

    fun editAppointmentBD(appointmentParams: AppointmentParams, db: SQLiteDatabase) {
        /** Update a row */
        var query: String?

        with(appointmentParams) {
            query = "UPDATE $TABLE_NAME SET " +
                    "$COLUMN_DATE = '${appointmentDate}', " +
                    "$COLUMN_START_TIME = '${startTime}', " +
                    "$COLUMN_PROCEDURE = '${procedure}', " +
                    "$COLUMN_NAME = '${clientName}', " +
                    "$COLUMN_PHONE = '${phoneNum}', " +
                    "$COLUMN_MISC = '${misc}', " +
                    "$COLUMN_DELETED = '${deleted}' " +
                    "WHERE $COLUMN_ID = ${_id};"
        }
        Log.e(log, String.format("Edit row query: $query"))
        db.execSQL(query)
        Log.e(log, String.format("Row was edited successfully"))
    }
}
