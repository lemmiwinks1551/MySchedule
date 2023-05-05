package com.example.projectnailsschedule.data.repository

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.example.projectnailsschedule.data.storage.ScheduleDb
import com.example.projectnailsschedule.data.storage.ScheduleDbHelper
import com.example.projectnailsschedule.domain.models.AppointmentModelDb
import com.example.projectnailsschedule.domain.models.DateParams
import com.example.projectnailsschedule.domain.repository.ScheduleRepository
import com.example.projectnailsschedule.util.Util
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.CountDownLatch

class ScheduleRepositoryImpl(context: Context) : ScheduleRepository {
    private var dbRoom = ScheduleDb.getDb(context)
    private var scheduleDbHelper: ScheduleDbHelper = ScheduleDbHelper(context)
    private var log = this::class.simpleName

    override fun saveAppointment(appointmentModelDb: AppointmentModelDb): Boolean {
        // Save appointment in database
        Thread {
            dbRoom.getDao().insert(appointmentModelDb)
        }.start()
        Log.e(log, "Appointment $appointmentModelDb saved")
        return true
    }

    override fun updateAppointment(appointmentModelDb: AppointmentModelDb): Boolean {
        // Edit appointment in database
        Thread {
            dbRoom.getDao().update(appointmentModelDb)
        }.start()
        Log.e(log, "Appointment $appointmentModelDb updated")
        return true
    }

    override fun getDateAppointments(dateParams: DateParams): Array<AppointmentModelDb> {
        var arrayOfAppointmentModelDbs = arrayOf<AppointmentModelDb>()

        Thread {
            val dateToCheck = Util().dateConverterNew(dateParams.date.toString())
            arrayOfAppointmentModelDbs = dbRoom.getDao().getDateAppointments(dateToCheck)
            Log.e(log, "$dateToCheck : ${arrayOfAppointmentModelDbs.size}")
        }.start()

        return arrayOfAppointmentModelDbs
    }

    override fun deleteAppointment(id: Int) {
        val db: SQLiteDatabase = scheduleDbHelper.writableDatabase
        scheduleDbHelper.deleteAppointment(
            currentId = id,
            db = db
        )
    }

    override fun getAllAppointments(): Cursor {
        val db: SQLiteDatabase = scheduleDbHelper.readableDatabase
        return scheduleDbHelper.getAllAppointments(
            db = db
        )
    }
}