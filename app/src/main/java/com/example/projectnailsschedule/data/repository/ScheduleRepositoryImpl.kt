package com.example.projectnailsschedule.data.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.asLiveData
import com.example.projectnailsschedule.data.storage.ScheduleDb
import com.example.projectnailsschedule.domain.models.AppointmentModelDb
import com.example.projectnailsschedule.domain.models.DateParams
import com.example.projectnailsschedule.domain.repository.ScheduleRepository
import com.example.projectnailsschedule.util.Util
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.toCollection
import kotlinx.coroutines.flow.toList

class ScheduleRepositoryImpl(context: Context) : ScheduleRepository {
    private var dbRoom = ScheduleDb.getDb(context)
    private var log = this::class.simpleName

    override fun saveAppointment(appointmentModelDb: AppointmentModelDb): Boolean {
        // Save appointment in database
        val thread = Thread {
            dbRoom.getDao().insert(appointmentModelDb)
        }
        thread.start()
        thread.join()
        Log.e(log, "Appointment $appointmentModelDb saved")
        return true
    }

    override fun updateAppointment(appointmentModelDb: AppointmentModelDb): Boolean {
        // Edit appointment in database
        val thread = Thread {
            dbRoom.getDao().update(appointmentModelDb)
        }
        thread.start()
        thread.join()
        Log.e(log, "Appointment $appointmentModelDb updated")
        return true
    }

    override fun getDateAppointments(dateParams: DateParams): Array<AppointmentModelDb> {
        // TODO: Add coroutines

        var arrayOfAppointmentModelDbs = arrayOf<AppointmentModelDb>()

        val thread = Thread {
            val dateToCheck = Util().dateConverterNew(dateParams.date.toString())
            dateParams.appointmentCount = dbRoom.getDao().getDateAppointments(dateToCheck).size
            arrayOfAppointmentModelDbs = dbRoom.getDao().getDateAppointments(dateToCheck)
            Log.e(log, "$dateParams")
        }
        thread.start()
        thread.join()

        return arrayOfAppointmentModelDbs
    }

    override fun deleteAppointment(appointmentModelDb: AppointmentModelDb) {
        val thread = Thread {
            dbRoom.getDao().delete(appointmentModelDb)
        }
        thread.start()
        thread.join()
        Log.e(log, "$appointmentModelDb deleted")
    }

    override fun getAllAppointments(): List<AppointmentModelDb> {
        var arrayOfAppointmentModelDbs = listOf<AppointmentModelDb>()
        val thread = Thread {
            arrayOfAppointmentModelDbs = dbRoom.getDao().selectAllList()
        }
        thread.start()
        thread.join()

        return arrayOfAppointmentModelDbs
    }
}