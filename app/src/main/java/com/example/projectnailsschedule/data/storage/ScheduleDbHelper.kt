package com.example.projectnailsschedule.data.storage

import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.projectnailsschedule.domain.models.AppointmentParams
import com.example.projectnailsschedule.domain.models.DateParams
import com.example.projectnailsschedule.util.WorkFolders
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.time.format.DateTimeFormatter
import java.util.*

/** Methods for interacting with Schedule database */

class ScheduleDbHelper(context: Context?) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, VERSION) {
    companion object {
        // Bd name
        var DATABASE_NAME = String.format("${WorkFolders().getFolderPath()}/schedule.db")

        // Current bd version
        private const val VERSION = 24

        // Table name
        const val TABLE_NAME = "schedule" // название таблицы в бд

        // Columns
        const val COLUMN_ID = "_id"
        const val COLUMN_DATE = "date"
        const val COLUMN_START_TIME = "start"
        const val COLUMN_PROCEDURE = "procedure"
        const val COLUMN_NAME = "name"
        const val COLUMN_PHONE = "phone"
        const val COLUMN_MISC = "misc"
        val LOG = this::class.simpleName
    }

    private val myContext = context

    fun saveAppointmentBD(appointmentParams: AppointmentParams, db: SQLiteDatabase) {
        /** Insert a new row */
        var query: String?

        with(appointmentParams) {
            query = "INSERT INTO $TABLE_NAME " +
                    "($COLUMN_DATE, $COLUMN_START_TIME, " +
                    "$COLUMN_PROCEDURE, $COLUMN_NAME, " +
                    "$COLUMN_PHONE, $COLUMN_MISC) " +
                    "VALUES " +
                    "('${appointmentDate}', '${startTime}', " +
                    "'${procedureName}', '${clientName}', " +
                    "'${phoneNum}', '${misc}');"
        }
        Log.e(LOG, String.format("Add row query: $query"))
        db.execSQL(query)
        Log.e(LOG, String.format("Add row - success"))
    }

    fun getDateAppointments(dateParams: DateParams, db: SQLiteDatabase): Cursor {
        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
        val query = "SELECT * FROM $TABLE_NAME WHERE $COLUMN_DATE = '${dateParams.date?.format(formatter)}';"
        Log.e(LOG, String.format("getDateAppointments query: $query"))
        return db.rawQuery(query, null)
    }

    fun deleteRow(currentId: Int, db: SQLiteDatabase) {
        /** Delete a row */
        val query = "DELETE FROM $TABLE_NAME WHERE $COLUMN_ID = $currentId;"
        Log.e(LOG, String.format("Delete row query: $query"))
        db.execSQL(query)
        Log.e(LOG, String.format("Delete row - success"))
    }

    fun getRow(day: String, db: SQLiteDatabase): Cursor {
        /** Select a row from a database */
        val query =
            "SELECT * FROM $TABLE_NAME WHERE $COLUMN_DATE = '$day' ORDER BY $COLUMN_START_TIME ASC;"
        Log.e(LOG, String.format("Row № $day fetched"))
        return db.rawQuery(query, null)
    }

    fun editAppointmentBD(appointmentParams: AppointmentParams, db: SQLiteDatabase) {
        /** Update a row */
        var query: String?

        with(appointmentParams) {
            query = "UPDATE $TABLE_NAME SET " +
                    "$COLUMN_DATE = '${appointmentDate}', " +
                    "$COLUMN_START_TIME = '${startTime}', " +
                    "$COLUMN_PROCEDURE = '${procedureName}', " +
                    "$COLUMN_NAME = '${clientName}', " +
                    "$COLUMN_PHONE = '${phoneNum}', " +
                    "$COLUMN_MISC = '${misc}' " +
                    "WHERE $COLUMN_ID = ${_id};"
        }

        Log.e(LOG, String.format("Edit row query: $query"))
        db.execSQL(query)
        Log.e(LOG, String.format("Row was edited successfully"))
    }

    override fun onCreate(db: SQLiteDatabase) {
        Log.e(LOG, "Creating database")
        db.execSQL(
            "CREATE TABLE $TABLE_NAME ($COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "$COLUMN_DATE TEXT NOT NULL, " +
                    "$COLUMN_START_TIME TEXT NOT NULL, " +
                    "$COLUMN_PROCEDURE TEXT NOT NULL, " +
                    "$COLUMN_NAME TEXT NOT NULL, " +
                    "$COLUMN_PHONE TEXT NOT NULL, " +
                    "$COLUMN_MISC TEXT);"
        )
        Log.e(LOG, "DB was created")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        Log.e(LOG, "Updating database")
        // TODO: Добавить логику, чтобы старая БД переписывалась в новую, а не убивалась
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
        Log.e(LOG, "Database was updated")
    }

    fun fetchNameDate(date: String, db: SQLiteDatabase): Cursor {
        // Get rows filtered by date
        val query = "SELECT * FROM $TABLE_NAME WHERE $COLUMN_DATE = '$date';"
        return db.rawQuery(query, null)
    }

    fun createDb() {
        // Method for search fragment
        val file = File(StatusDbHelper.DATABASE_NAME)
        if (!file.exists()) {
            //получаем локальную бд как поток
            try {
                myContext?.assets?.open(StatusDbHelper.DATABASE_NAME).use { myInput ->
                    FileOutputStream(StatusDbHelper.DATABASE_NAME).use { myOutput ->

                        // побайтово копируем данные
                        val buffer = ByteArray(1024)
                        var length: Int
                        if (myInput != null) {
                            while (myInput.read(buffer).also { length = it } > 0) {
                                myOutput.write(buffer, 0, length)
                            }
                        }
                        myOutput.flush()
                    }
                }
            } catch (e: IOException) {
                Log.d(LOG, e.toString())
            }
        }
    }

    @Throws(SQLException::class)
    fun open(): SQLiteDatabase? {
        return SQLiteDatabase.openDatabase(DATABASE_NAME, null, SQLiteDatabase.OPEN_READWRITE)
    }

}
