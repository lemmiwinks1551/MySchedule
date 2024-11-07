package com.example.projectnailsschedule.presentation.calendar

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.projectnailsschedule.R
import com.example.projectnailsschedule.domain.models.AppointmentModelDb
import com.example.projectnailsschedule.domain.models.CalendarDateModelDb
import com.example.projectnailsschedule.domain.models.DateParams
import com.example.projectnailsschedule.domain.models.ProductionCalendarDateModel
import com.example.projectnailsschedule.domain.models.UserDataManager
import com.example.projectnailsschedule.domain.usecase.apiUC.GetProductionCalendarDateInfoUseCase
import com.example.projectnailsschedule.domain.usecase.apiUC.GetProductionCalendarYearUseCase
import com.example.projectnailsschedule.domain.usecase.apiUC.serverSyncUC.GetByLocalAppointmentIdUseCase
import com.example.projectnailsschedule.domain.usecase.apiUC.serverSyncUC.UpdateAppointmentDtoUseCase
import com.example.projectnailsschedule.domain.usecase.appointmentUC.DeleteAppointmentUseCase
import com.example.projectnailsschedule.domain.usecase.appointmentUC.InsertAppointmentUseCase
import com.example.projectnailsschedule.domain.usecase.appointmentUC.SearchAppointmentUseCase
import com.example.projectnailsschedule.domain.usecase.appointmentUC.UpdateAppointmentUseCase
import com.example.projectnailsschedule.domain.usecase.calendarUC.CalendarDbDeleteObj
import com.example.projectnailsschedule.domain.usecase.calendarUC.GetDateAppointments
import com.example.projectnailsschedule.domain.usecase.calendarUC.InsertCalendarDateUseCase
import com.example.projectnailsschedule.domain.usecase.calendarUC.SelectCalendarDateByDateUseCase
import com.example.projectnailsschedule.domain.usecase.socUC.StartInstagramUc
import com.example.projectnailsschedule.domain.usecase.socUC.StartPhoneUc
import com.example.projectnailsschedule.domain.usecase.socUC.StartTelegramUc
import com.example.projectnailsschedule.domain.usecase.socUC.StartVkUc
import com.example.projectnailsschedule.domain.usecase.socUC.StartWhatsAppUc
import com.example.projectnailsschedule.presentation.calendar.calendarRecyclerView.CalendarRvAdapter
import com.example.projectnailsschedule.util.Util
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class DateParamsViewModel @Inject constructor(
    private val getDateAppointments: GetDateAppointments,
    private val selectCalendarDateByDateUseCase: SelectCalendarDateByDateUseCase,
    private val insertCalendarDateUseCase: InsertCalendarDateUseCase,
    private val calendarDbDeleteObj: CalendarDbDeleteObj,
    private var insertAppointmentUseCase: InsertAppointmentUseCase,
    private val updateAppointmentUseCase: UpdateAppointmentUseCase,
    private var searchAppointmentUseCase: SearchAppointmentUseCase,
    private var deleteAppointmentUseCase: DeleteAppointmentUseCase,
    private val startVkUc: StartVkUc,
    private val startTelegramUc: StartTelegramUc,
    private val startInstagramUc: StartInstagramUc,
    private val startWhatsAppUc: StartWhatsAppUc,
    private val startPhoneUc: StartPhoneUc,
    private val getProductionCalendarDateInfoUseCase: GetProductionCalendarDateInfoUseCase,
    private val getProductionCalendarYearUseCase: GetProductionCalendarYearUseCase,
    private val updateAppointmentDtoUseCase: UpdateAppointmentDtoUseCase,
    private val getByLocalAppointmentIdUseCase: GetByLocalAppointmentIdUseCase
) : ViewModel() {
    private val tagDateColor = "DateColor"

    var oldPosition: Int = 0

    // var updates when click at day or month in calendar
    var selectedDate = MutableLiveData(
        DateParams(
            date = LocalDate.now()
        )
    )

    val dateInfo = MutableLiveData<String?>(null)

    // position of appointments in selectedDate.appointmentsList to edit
    // if position == null - create new appointment
    var appointmentPosition: Int? = null

    var previousDate = MutableLiveData(DateParams())

    var dateDetailsVisibility = MutableLiveData(false)

    var prevCalendarRvHolder: CalendarRvAdapter.ViewHolder? = null

    suspend fun getArrayOfAppointments(date: LocalDate): MutableList<AppointmentModelDb> {
        return getDateAppointments.execute(date)
    }

    fun updateSelectedDate(dateParams: DateParams) {
        val updatedDateParams =
            selectedDate.value?.copy(
                date = dateParams.date,
                appointmentsList = dateParams.appointmentsList
            )
        selectedDate.postValue(updatedDateParams)
    }

    fun changeMonth(operator: Boolean) {
        val updatedDateParams: DateParams = when (operator) {
            true -> selectedDate.value?.copy(
                date = selectedDate.value?.date?.plusMonths(1)
            )!!

            false -> selectedDate.value?.copy(
                date = selectedDate.value?.date?.minusMonths(1)
            )!!
        }

        selectedDate.postValue(updatedDateParams)
        dateDetailsVisibility.postValue(false)
        dateInfo.postValue(null)
    }

    private suspend fun selectCalendarDateByDate(date: String): CalendarDateModelDb {
        return selectCalendarDateByDateUseCase.execute(date)
    }

    suspend fun getDateId(ruFormatDate: String): Int? {
        Log.d(tagDateColor, "Getting color for $ruFormatDate")
        return try {
            val deferredColor = CoroutineScope(Dispatchers.IO).async {
                selectCalendarDateByDate(ruFormatDate)
            }

            Log.d(tagDateColor, "Date $ruFormatDate has color ${deferredColor.await()._id}")
            deferredColor.await()._id
        } catch (e: Exception) {
            null
        }
    }

    suspend fun getDateColor(ruFormatDate: String): String? {
        Log.d(tagDateColor, "Getting color for $ruFormatDate")

        // Запускаем асинхронную операцию в фоновом потоке
        val calendarDate = selectCalendarDateByDate(ruFormatDate)

        // Проверяем результат на null перед вызовом getColor()
        val color: String? = calendarDate?.color // Не убирать "?"

        Log.d(tagDateColor, "Date $ruFormatDate has color $color")
        return color
    }

    suspend fun insertCalendarDate(calendarDateModelDb: CalendarDateModelDb): Boolean {
        return insertCalendarDateUseCase.execute(calendarDateModelDb)
    }

    suspend fun calendarDbDeleteObj(calendarDateModelDb: CalendarDateModelDb): Boolean {
        return calendarDbDeleteObj.execute(calendarDateModelDb)
    }

    suspend fun insertAppointment(
        appointmentModelDb: AppointmentModelDb,
        position: Int = -1
    ): Long {
        if (position == -1) {
            selectedDate.value?.appointmentsList?.add(appointmentModelDb)
        } else {
            selectedDate.value!!.appointmentsList?.add(
                position,
                appointmentModelDb
            )
        }
        selectedDate.postValue(selectedDate.value)
        return insertAppointmentUseCase.execute(appointmentModelDb)
    }

    suspend fun updateAppointment(appointmentModelDb: AppointmentModelDb): Boolean {
        selectedDate.value?.appointmentsList?.set(
            appointmentPosition!!,
            appointmentModelDb
        )
        selectedDate.postValue(selectedDate.value)
        return updateAppointmentUseCase.execute(appointmentModelDb)
    }

    suspend fun updateAppointmentSyncDb(appointmentModelDb: AppointmentModelDb) {
        val appointmentDto = getByLocalAppointmentIdUseCase.execute(appointmentModelDb._id!!)

        with(appointmentDto) {
            syncTimestamp = Util().generateTimestamp()
            syncStatus = "NotSynchronized"
            appointmentDate = appointmentModelDb.date
            appointmentTime = appointmentModelDb.time
            appointmentNotes = appointmentModelDb.notes

            clientId = appointmentModelDb.clientId.toString()
            clientName = appointmentModelDb.name
            clientPhone = appointmentModelDb.phone
            clientTelegram = appointmentModelDb.telegram
            clientInstagram = appointmentModelDb.instagram
            clientVk = appointmentModelDb.vk
            clientWhatsapp = appointmentModelDb.whatsapp
            clientNotes = appointmentModelDb.clientNotes
            clientPhoto = appointmentModelDb.photo

            procedureId = null
            procedureName = appointmentModelDb.procedure
            procedurePrice = appointmentModelDb.procedurePrice
            procedureNotes = appointmentModelDb.procedureNotes
        }
        updateAppointmentDtoUseCase.execute(appointmentDto)
    }

    suspend fun searchAppointment(searchQuery: String): MutableList<AppointmentModelDb> {
        return searchAppointmentUseCase.execute(searchQuery)
    }

    suspend fun deleteAppointment(
        appointmentModelDb: AppointmentModelDb,
        position: Int = -1
    ) {
        deleteAppointmentUseCase.execute(appointmentModelDb)
        if (position == -1) {
            selectedDate.value!!.appointmentsList?.removeAt(position)
        } else {
            selectedDate.value!!.appointmentsList?.remove(appointmentModelDb)
        }
        selectedDate.postValue(selectedDate.value)
    }

    fun getAppointmentPositionInDate(
        appointmentModelDb: AppointmentModelDb,
        appointmentList: MutableList<AppointmentModelDb>
    ): Int {
        return appointmentList.indexOf(appointmentModelDb)
    }

    fun getHolidayIcon(note: String): Int {
        when (note) {
            "Новогодние каникулы" -> return R.drawable.new_year_icon
            // "Рождество Христово" -> return R.drawable.christmas_icon
            "День защитника Отечества" -> return R.drawable._23feb_icon
            "Международный женский день" -> return R.drawable.international_womens_day
            "Праздник Весны и Труда" -> return R.drawable.hammer_sickle
            "День Победы" -> return R.drawable.victory_day
            "День России" -> return R.drawable.russianflag
            "День народного единства" -> return R.drawable.noto_people_holding_hands
        }
        return R.drawable.asterisk
    }

    fun startVk(uri: String) {
        startVkUc.execute(uri)
    }

    fun startTelegram(uri: String) {
        startTelegramUc.execute(uri)
    }

    fun startInstagram(uri: String) {
        startInstagramUc.execute(uri)
    }

    fun startWhatsApp(uri: String) {
        startWhatsAppUc.execute(uri)
    }

    fun startPhone(phoneNum: String) {
        startPhoneUc.execute(phoneNum)
    }

    fun getSelectedMonth(): LocalDate? {
        return selectedDate.value?.date
    }

    fun updateUserData(event: String) {
        UserDataManager.updateUserData(event = event)
    }

    suspend fun getDataInfo(dayNum: Int): ProductionCalendarDateModel {
        // Вернуть информацию о дате из cache ProductionCalendar
        return getProductionCalendarDateInfoUseCase.execute(selectedDate.value!!, dayNum)
    }

    suspend fun getYearProcedureCalendar(year: String) {
        // Вернуть информацию о годе из API ProductionCalendar
        getProductionCalendarYearUseCase.execute(year = year)
    }
}