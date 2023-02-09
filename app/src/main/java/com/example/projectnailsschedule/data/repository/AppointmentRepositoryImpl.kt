package com.example.projectnailsschedule.data.repository

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.example.projectnailsschedule.data.storage.ScheduleDbHelper
import com.example.projectnailsschedule.domain.models.AppointmentParams
import com.example.projectnailsschedule.domain.models.DateParams
import com.example.projectnailsschedule.domain.repository.AppointmentRepository

class AppointmentRepositoryImpl(context: Context?): AppointmentRepository {
    private var scheduleDbHelper: ScheduleDbHelper = ScheduleDbHelper(context)
    private var db: SQLiteDatabase = scheduleDbHelper.writableDatabase

    override fun saveAppointment(appointmentParams: AppointmentParams): Boolean {
        /** Save appointment in database */
        scheduleDbHelper.saveAppointmentBD(appointmentParams, db)
        db.close()
        return true
    }

    override fun editAppointment(appointmentParams: AppointmentParams): Boolean {
        /** Edit appointment in database */
        scheduleDbHelper.editAppointmentBD(appointmentParams, db)
        db.close()
        return true
    }

    override fun getDateAppointments(dateParams: DateParams): Cursor {
        val db: SQLiteDatabase = scheduleDbHelper.readableDatabase
        return scheduleDbHelper.getDateAppointments(
            dateParams = dateParams,
            db = db
        )
    }

}