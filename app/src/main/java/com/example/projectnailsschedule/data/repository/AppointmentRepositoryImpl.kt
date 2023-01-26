package com.example.projectnailsschedule.data.repository

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.example.projectnailsschedule.data.ScheduleDbHelper
import com.example.projectnailsschedule.domain.models.AppointmentParams
import com.example.projectnailsschedule.domain.repository.AppointmentRepository

class AppointmentRepositoryImpl(private var context: Context?): AppointmentRepository {

    private lateinit var databaseHelper: ScheduleDbHelper
    private lateinit var db: SQLiteDatabase

    override fun saveAppointment(appointmentParams: AppointmentParams): Boolean {
        /** Save appointment in database */
        databaseHelper = ScheduleDbHelper(context)
        db = databaseHelper.writableDatabase
        databaseHelper.saveAppointmentBD(appointmentParams, db)
        db.close()
        return true
    }

    override fun editAppointment(appointmentParams: AppointmentParams): Boolean {
        /** Edit appointment in database */
        databaseHelper = ScheduleDbHelper(context)
        db = databaseHelper.writableDatabase
        databaseHelper.editAppointmentBD(appointmentParams, db)
        db.close()
        return true
    }
}