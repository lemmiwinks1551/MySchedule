package com.example.projectnailsschedule.presentation.main

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.projectnailsschedule.domain.models.AppointmentModelDb
import com.example.projectnailsschedule.domain.models.CalendarDateModelDb
import com.example.projectnailsschedule.domain.models.UserDataManager
import com.example.projectnailsschedule.domain.models.dto.UserInfoDto
import com.example.projectnailsschedule.domain.models.dto.UserInfoDtoManager
import com.example.projectnailsschedule.domain.usecase.account.GetJwt
import com.example.projectnailsschedule.domain.usecase.account.GetUserInfoApiUseCase
import com.example.projectnailsschedule.domain.usecase.apiUC.SendUserDataUseCase
import com.example.projectnailsschedule.domain.usecase.apiUC.localSyncDbUC.GetBySyncUuidUseCase
import com.example.projectnailsschedule.domain.usecase.apiUC.localSyncDbUC.GetDeletedAppointmentsUseCase
import com.example.projectnailsschedule.domain.usecase.apiUC.localSyncDbUC.GetMaxAppointmentsTimestamp
import com.example.projectnailsschedule.domain.usecase.apiUC.localSyncDbUC.GetNotSyncAppointmentsUseCase
import com.example.projectnailsschedule.domain.usecase.apiUC.serverSyncUC.DeleteRemoteAppointmentUseCase
import com.example.projectnailsschedule.domain.usecase.apiUC.serverSyncUC.GetLastRemoteAppointmentTimestamp
import com.example.projectnailsschedule.domain.usecase.apiUC.serverSyncUC.GetUserRemoteAppointmentsAfterTimestampUseCase
import com.example.projectnailsschedule.domain.usecase.apiUC.serverSyncUC.PostAppointmentUseCase
import com.example.projectnailsschedule.domain.usecase.apiUC.serverSyncUC.serverCalendarColorApiUC.DeleteRemoteCalendarDateUseCase
import com.example.projectnailsschedule.domain.usecase.apiUC.serverSyncUC.serverCalendarColorApiUC.GetAfterTimestampCalendarDateUseCase
import com.example.projectnailsschedule.domain.usecase.apiUC.serverSyncUC.serverCalendarColorApiUC.GetLastRemoteTimestampCalendarDateUseCase
import com.example.projectnailsschedule.domain.usecase.apiUC.serverSyncUC.serverCalendarColorApiUC.PostCalendarDateUseCase
import com.example.projectnailsschedule.domain.usecase.appointmentUC.DeleteAppointmentUseCase
import com.example.projectnailsschedule.domain.usecase.appointmentUC.InsertAppointmentUseCase
import com.example.projectnailsschedule.domain.usecase.appointmentUC.UpdateAppointmentUseCase
import com.example.projectnailsschedule.domain.usecase.calendarUC.DeleteCalendarDateUseCase
import com.example.projectnailsschedule.domain.usecase.calendarUC.GetBySyncUuidCalendarDateUseCase
import com.example.projectnailsschedule.domain.usecase.calendarUC.GetDeletedCalendarDateUseCase
import com.example.projectnailsschedule.domain.usecase.calendarUC.GetMaxCalendarDateTimestamp
import com.example.projectnailsschedule.domain.usecase.calendarUC.GetNotSyncCalendarDateUseCase
import com.example.projectnailsschedule.domain.usecase.calendarUC.InsertCalendarDateUseCase
import com.example.projectnailsschedule.domain.usecase.calendarUC.UpdateCalendarDateUseCase
import com.example.projectnailsschedule.domain.usecase.settingsUC.GetUserThemeUseCase
import com.example.projectnailsschedule.util.Util
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.Date
import javax.inject.Inject

const val localOutdated = "Локальные данные устарели!"
const val remoteOutdated = "Удаленные данные устарели!"
const val localAndRemoteSynchronized = "Данные синхронизированы"
const val synchronizedStatus = "Synchronized"
const val notSynchronizedStatus = "NotSynchronized"
const val deletedStatus = "DELETED"
const val okServerResponse = "200"

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getUserThemeUseCase: GetUserThemeUseCase,
    private val sendUserDataUseCase: SendUserDataUseCase,
    private var getUserInfoApi: GetUserInfoApiUseCase,
    private var getJwt: GetJwt,

    // Appointments DB
    private var insertAppointmentUseCase: InsertAppointmentUseCase,
    private val updateAppointmentUseCase: UpdateAppointmentUseCase,
    private var deleteAppointmentUseCase: DeleteAppointmentUseCase,
    private var getNotSyncAppointmentsUseCase: GetNotSyncAppointmentsUseCase,
    private var getDeletedAppointmentsUseCase: GetDeletedAppointmentsUseCase,
    private var getMaxAppointmentsTimestamp: GetMaxAppointmentsTimestamp,
    private var getGetBySyncUuidUseCase: GetBySyncUuidUseCase,

    // Appointments API
    private var postAppointmentUseCase: PostAppointmentUseCase,
    private var deleteRemoteAppointmentUseCase: DeleteRemoteAppointmentUseCase,
    private var getUserLastRemoteAppointmentTimestamp: GetLastRemoteAppointmentTimestamp,
    private var getUserRemoteAppointmentsAfterTimestampUseCase: GetUserRemoteAppointmentsAfterTimestampUseCase,

    // CalendarDate DB:
    private var insertCalendarDateUseCase: InsertCalendarDateUseCase,
    private var deleteCalendarDateUseCase: DeleteCalendarDateUseCase,
    private var updateCalendarDateUseCase: UpdateCalendarDateUseCase,
    private var getNotSyncCalendarDateUseCase: GetNotSyncCalendarDateUseCase,
    private var getDeletedCalendarDateUseCase: GetDeletedCalendarDateUseCase,
    private var getMaxCalendarDateTimestamp: GetMaxCalendarDateTimestamp,
    private var getBySyncUuidCalendarDateUseCase: GetBySyncUuidCalendarDateUseCase,

    // CalendarDate API
    private var postRemoteCalendarDateUseCase: PostCalendarDateUseCase,
    private var deleteRemoteCalendarDateUseCase: DeleteRemoteCalendarDateUseCase,
    private var getLastRemoteTimestampCalendarDateUseCase: GetLastRemoteTimestampCalendarDateUseCase,
    private var getRemoteAfterTimestampCalendarDateUseCase: GetAfterTimestampCalendarDateUseCase,
) : ViewModel() {
    private val logAppointments = "SyncAppointments"
    private val logCalendar = "SyncCalendar"

    val syncStatus = MutableLiveData(false)
    val menuStatus = MutableLiveData(false)

    suspend fun getUserInfoApi(): UserInfoDto? {
        val jwt = getJwt.execute() ?: return null
        val username = Util().extractUsernameFromJwt(jwt) ?: return null
        val userInfo = getUserInfoApi.execute(username, jwt)?.body() ?: return null

        UserInfoDtoManager.setUserDto(userInfo)
        return userInfo
    }

    suspend fun sendUserData() {
        val userData = UserDataManager.pollUserDateQueue()
        if (userData != null) {
            sendUserDataUseCase.execute(userData)
        }
    }

    fun getUserTheme(): String {
        return getUserThemeUseCase.execute()
    }

    // Appointments
    suspend fun synchronizationAppointments() {
        // Получаем юзера, если пользователь не залогинился - выходим
        val user = getUserInfoApi()
        val jwt = getJwt.execute()
        if (!checkSyncConditions(user, jwt)) {
            return
        }

        // Получаем временные метки последней записи локально и удаленно
        val lastLocalTimestamp = getMaxAppointmentsTimestamp.execute() ?: 0L
        val lastRemoteTimestamp =
            getUserLastRemoteAppointmentTimestamp.execute(user!!, jwt!!) ?: 0L

        when (checkLastTimestamp(lastLocalTimestamp, lastRemoteTimestamp)) {
            localOutdated -> {
                pullAppointments(lastLocalTimestamp, jwt)
            }

            remoteOutdated -> {
                pushAppointments(user)
            }
        }
    }

    private suspend fun pushAppointments(user: UserInfoDto) {
        Log.i(logAppointments, "Выполняем метод pushAppointments()")
        /**
         *  1.  Выбирает все записи со статусом NotSynchronized
         *  1.1 Отправляет все записи на сервер
         *  1.2 Если, запись успешно обновлена на сервер (код 200) -> устанавливает статус Synchronized */

        /**
         *  2.  Выбирает все записи со статусом DELETED
         *  2.1 Отправляет все записи на сервер
         *  2.2 Если, запись успешно удалена на сервер (код 200) -> удаляет запись локально */

        // Получаем записи, которые еще не были синхронизированы (syncStatus = "NotSynchronized")
        val notSyncedData = getNotSyncAppointmentsUseCase.execute()

        // Получаем записи, которые были удалены (syncStatus = "DELETED")
        val deletedData = getDeletedAppointmentsUseCase.execute()

        // Создаем общий лист несинхронизированных и удаленных записей
        val notSyncedAndDeletedData = notSyncedData + deletedData

        // Устанавливаем имя пользователя для записей, у которых оно отсутствует
        setUsernameAppointments(notSyncedAndDeletedData, user)

        // Отправляем несинхронизированные и удаленные записи на сервер
        for (appointment in notSyncedAndDeletedData) {
            if (appointment.syncStatus == notSynchronizedStatus) {
                val result = postAppointmentUseCase.execute(appointment, getJwt.execute()!!)
                if (result == okServerResponse) {
                    // Обновляем статус синхронизации локально, если запись успешно обновлена на сервере
                    appointment.syncStatus = synchronizedStatus
                    updateAppointmentUseCase.execute(appointment)
                }
            }

            if (appointment.syncStatus == deletedStatus) {
                val result = deleteRemoteAppointmentUseCase.execute(appointment, getJwt.execute()!!)
                if (result == okServerResponse) {
                    // Удаляем запись локально, если она успешно удалена на сервере
                    deleteAppointmentUseCase.execute(appointment)
                }
            }
        }
    }

    private suspend fun pullAppointments(timestamp: Long, jwt: String) {
        Log.i(logAppointments, "Выполняем метод pullAppointments()")
        /**
         *  1.  Получает с сервера все записи, у которых дата ПОЗЖЕ указаной в timestamp
         *  2.
         *  3.   */

        // Получаем все данные, у которых дата обновления > последней даты обновления в локальной БД
        val appointmentsToUpdate = getUserRemoteAppointmentsAfterTimestampUseCase.execute(
            jwt, timestamp
        )

        if (appointmentsToUpdate != null) {
            Log.i(logAppointments, "Данные для обновления получены")
            updateAppointmentsDb(appointmentsToUpdate)
        } else {
            Log.i(logAppointments, "Данные для обновления не получены")
        }
    }

    private suspend fun updateAppointmentsDb(appointments: List<AppointmentModelDb>) {
        Log.i(logAppointments, "Выполняем метод updateAppointmentsDb()")
        Log.i(
            logAppointments,
            "Вносим данные в локальную БД. Записей для обработки: ${appointments.size}"
        )

        for (updatedAppointment in appointments) {
            // проверяем, существует ли такая запись уже по uuid
            if (updatedAppointment.syncUUID == null) {
                continue
            }

            // ищем такую запись локально по syncUUID
            val localAppointment = getGetBySyncUuidUseCase.execute(updatedAppointment.syncUUID!!)

            // Если локальной записи такой нет - создаем её
            if (localAppointment == null) {
                Log.i(logAppointments, "Вносим данные в локальную БД (новая запись)")
                if (updatedAppointment.syncStatus!! == "NotSynchronized") {
                    // Обнуляем id, чтобы не затереть другую запись
                    val newAppointment =
                        updatedAppointment.copy(_id = null, syncStatus = "Synchronized")

                    insertAppointmentUseCase.execute(newAppointment)
                }

                if (updatedAppointment.syncStatus!! == "DELETED") {
                    Log.i(logAppointments, "Запись удалена, не обновляем")
                    continue
                }
            }

            // Если запись уже существует - сверяем даты, если дата "свежее" - заменяем
            if (localAppointment != null) {
                if (updatedAppointment.syncTimestamp!! > localAppointment.syncTimestamp!!) {
                    Log.i(logAppointments, "Вносим данные в локальную БД (обновляем запись)")

                    if (localAppointment.syncStatus == "DELETED") {
                        Log.i(
                            logAppointments,
                            "Запись, которую необходимо обновить была удалена локально. " + "Обновление отменяется. Удаляем запись с сервера"
                        )
                        // Ставим Статус DELETED на сервер
                        val result = deleteRemoteAppointmentUseCase.execute(
                            updatedAppointment, getJwt.execute()!!
                        )
                        if (result == "200") {
                            // Если Статус DELETED на сервере установлен - удаляем из локальной БД
                            deleteAppointmentUseCase.execute(localAppointment)
                        }
                        return
                    }

                    if (updatedAppointment.syncStatus!! == "NotSynchronized") {
                        // Если запись была обновлена - обновляем её во всех БД
                        Log.i(
                            logCalendar,
                            "Статус на сервере NotSynchronized, обновляем локальную запись"
                        )
                        updatedAppointment._id = localAppointment._id

                        // Заглушка, пока не настроена синхронизация клиентов - не обновляем поле clientID
                        updatedAppointment.clientId = localAppointment.clientId

                        updatedAppointment.syncStatus = "Synchronized"
                        updateAppointmentUseCase.execute(updatedAppointment)
                    }

                    if (updatedAppointment.syncStatus!! == "DELETED") {
                        Log.i(
                            logAppointments,
                            "Статус на сервере DELETED, удаляем локальную запись"
                        )
                        deleteAppointmentUseCase.execute(localAppointment)
                    }
                }
            }
        }
    }

    // Устанавливает имя пользователя для записей без него перед синхронизацией
    private suspend fun setUsernameAppointments(
        notSyncedAppointments: List<AppointmentModelDb>, user: UserInfoDto
    ) {
        // Добавляем имя пользователя ко всем записям, у которых оно отсутствует
        for (appointment in notSyncedAppointments) {
            if (appointment.userName == null) {
                appointment.userName = user.username
                updateAppointmentUseCase.execute(appointment)
            }
        }
    }

    // CalendarDate
    suspend fun synchronizationCalendarDate() {
        // Получаем юзера, если пользователь не залогинился - выходим
        val user = getUserInfoApi()
        val jwt = getJwt.execute()
        if (!checkSyncConditions(user, jwt)) {
            return
        }

        // Получаем временные метки последней записи локально и удаленно
        val lastLocalTimestamp = getMaxCalendarDateTimestamp.execute() ?: 0L
        val lastRemoteTimestamp =
            getLastRemoteTimestampCalendarDateUseCase.execute(user!!, jwt!!) ?: 0L

        when (checkLastTimestamp(lastLocalTimestamp, lastRemoteTimestamp)) {
            localOutdated -> {
                pullCalendarDate(lastLocalTimestamp, jwt)
            }

            remoteOutdated -> {
                pushCalendarDate(user)
            }
        }
    }

    private suspend fun pushCalendarDate(user: UserInfoDto) {
        Log.i(logAppointments, "Выполняем метод pushCalendarDate()")
        /**
         *  1.  Выбирает все записи со статусом NotSynchronized
         *  1.1 Отправляет все записи на сервер
         *  1.2 Если, запись успешно обновлена на сервер (код 200) -> устанавливает статус Synchronized */

        /**
         *  1.  Выбирает все записи со статусом DELETED
         *  2.1 Отправляет все записи на сервер
         *  2.2 Если, запись успешно удалена на сервер (код 200) -> удаляет запись локально */

        // Получаем записи, которые еще не были синхронизированы (syncStatus = "NotSynchronized")
        val notSyncedData = getNotSyncCalendarDateUseCase.execute()

        // Получаем записи, которые были удалены (syncStatus = "DELETED")
        val deletedData = getDeletedCalendarDateUseCase.execute()

        // Создаем общий лист несинхронизированных и удаленных записей
        val notSyncedAndDeletedData = notSyncedData + deletedData

        // Устанавливаем имя пользователя для записей, у которых оно отсутствует
        setUsernameCalendarDate(notSyncedAndDeletedData, user)

        // Отправляем несинхронизированные и удаленные записи на сервер
        for (calendarDateModelDb in notSyncedAndDeletedData) {
            if (calendarDateModelDb.syncStatus == notSynchronizedStatus) {
                val result = postRemoteCalendarDateUseCase.execute(calendarDateModelDb, getJwt.execute()!!)
                if (result == okServerResponse) {
                    // Обновляем статус синхронизации локально, если запись успешно обновлена на сервере
                    calendarDateModelDb.syncStatus = synchronizedStatus
                    updateCalendarDateUseCase.execute(calendarDateModelDb)
                }
            }

            if (calendarDateModelDb.syncStatus == deletedStatus) {
                val result = deleteRemoteCalendarDateUseCase.execute(calendarDateModelDb, getJwt.execute()!!)
                if (result == okServerResponse) {
                    // Удаляем запись локально, если она успешно удалена на сервере
                    deleteCalendarDateUseCase.execute(calendarDateModelDb)
                }
            }
        }
    }

    private suspend fun pullCalendarDate(timestamp: Long, jwt: String) {
        Log.i(logAppointments, "Выполняем метод pullCalendarDate()")
        /**
         *  1.  Получает с сервера все записи, у которых дата ПОЗЖЕ указаной в timestamp
         *  2.
         *  3.   */

        // Получаем все данные, у которых дата обновления > последней даты обновления в локальной БД
        val calendarDatesToUpdate = getRemoteAfterTimestampCalendarDateUseCase.execute(
            jwt, timestamp
        )

        if (calendarDatesToUpdate != null) {
            Log.i(logCalendar, "Данные для обновления получены")
            updateCalendarDateDb(calendarDatesToUpdate)
        } else {
            Log.i(logCalendar, "Данные для обновления не получены")
        }
    }

    private suspend fun updateCalendarDateDb(calendarDateToUpdate: List<CalendarDateModelDb>) {
        Log.i(logAppointments, "Выполняем метод updateAppointmentsDb()")
        Log.i(
            logAppointments,
            "Вносим данные в локальную БД. Записей для обработки: ${calendarDateToUpdate.size}"
        )

        for (updatedCalendarDate in calendarDateToUpdate) {
            // проверяем, существует ли такая запись уже по uuid
            if (updatedCalendarDate.syncUUID == null) {
                continue
            }

            val localCalendarDate =
                getBySyncUuidCalendarDateUseCase.execute(updatedCalendarDate.syncUUID)

            // Если локальной записи такой нет - создаем её
            if (localCalendarDate == null) {
                Log.i(logCalendar, "Вносим данные в локальную БД (новая запись)")
                if (updatedCalendarDate.syncStatus!! == "NotSynchronized") {
                    // Обнуляем id, чтобы не затереть другую запись
                    val newLocalCalendarDate =
                        updatedCalendarDate.copy(_id = null, syncStatus = "Synchronized")

                    insertCalendarDateUseCase.execute(newLocalCalendarDate)
                }

                if (updatedCalendarDate.syncStatus!! == "DELETED") {
                    Log.i(logAppointments, "Запись удалена, не обновляем")
                    continue
                }
            }

            // Если запись уже существует - сверяем даты, если дата "свежее" - заменяем
            if (localCalendarDate != null) {
                if (updatedCalendarDate.syncTimestamp!! > localCalendarDate.syncTimestamp!!) {
                    Log.i(logCalendar, "Вносим данные в локальную БД (обновляем запись)")

                    if (localCalendarDate.syncStatus == "DELETED") {
                        Log.i(
                            logCalendar,
                            "Запись, которую необходимо обновить была удалена локально. " + "Обновление отменяется. Удаляем запись с сервера"
                        )
                        // Ставим Статус DELETED на сервер
                        val result = deleteRemoteCalendarDateUseCase.execute(
                            updatedCalendarDate, getJwt.execute()!!
                        )
                        if (result == "200") {
                            // Если Статус DELETED на сервере установлен - удаляем из локальной бд
                            deleteCalendarDateUseCase.execute(localCalendarDate)
                        }
                        return
                    }

                    if (updatedCalendarDate.syncStatus!! == "NotSynchronized") {
                        // Если запись была обновлена - обновляем её в БД
                        Log.i(
                            logCalendar,
                            "Статус на сервере NotSynchronized, обновляем локальную запись"
                        )
                        val calendarDateForInput = updatedCalendarDate.copy(
                            _id = localCalendarDate._id,
                            syncStatus = "Synchronized"
                        )
                        updateCalendarDateUseCase.execute(calendarDateForInput)
                    }

                    if (updatedCalendarDate.syncStatus!! == "DELETED") {
                        Log.i(logCalendar, "Статус на сервере DELETED, удаляем локальную запись")
                        deleteCalendarDateUseCase.execute(localCalendarDate)
                    }
                }
            }
        }
    }

    // Устанавливает имя пользователя для записей без него перед синхронизацией
    private suspend fun setUsernameCalendarDate(
        notSyncedList: List<CalendarDateModelDb>, user: UserInfoDto
    ) {
        // Добавляем имя пользователя ко всем записям, у которых оно отсутствует
        for (calendarDateModel in notSyncedList) {
            if (calendarDateModel.userName == null) {
                calendarDateModel.userName = user.username
                updateCalendarDateUseCase.execute(calendarDateModel)
            }
        }
    }


    // Common
    private fun checkSyncConditions(user: UserInfoDto?, jwt: String?): Boolean {
        if (user == null) {
            // Если пользователь не залогинился
            // устанавливаем статус false и выходим
            syncStatus.postValue(false)
            return false
        }

        if (user.syncEnabled == false) {
            // Если у пользователя установлен syncEnabled == false
            // устанавливаем статус false и выходим
            syncStatus.postValue(false)
            return false
        }

        if (jwt == null) {
            // Если у пользователя нет токена - выходим
            syncStatus.postValue(false)
            return false
        }

        syncStatus.postValue(true)
        return true
    }

    private fun checkLastTimestamp(lastLocalTimestamp: Long, lastRemoteTimestamp: Long): String {
        // Проверка, устарели ли удаленные данные
        if (lastLocalTimestamp > lastRemoteTimestamp) {
            Log.i(logAppointments, "Удаленные данные устарели - отправляем данные на сервера")
            Log.i(
                logAppointments,
                "Последнее локальное обновление: ${Date(lastLocalTimestamp)} " + "Последнее удаленное обновление: ${
                    Date(lastRemoteTimestamp)
                } "
            )
            return remoteOutdated
        }

        // Проверка, устарели ли локальные данные
        if (lastLocalTimestamp < lastRemoteTimestamp) {
            Log.i(logAppointments, "Локальные данные устарели - получаем данные с сервера")
            Log.i(
                logAppointments,
                "Последнее локальное обновление: ${Date(lastLocalTimestamp)} " + "Последнее удаленное обновление: ${
                    Date(lastRemoteTimestamp)
                } "
            )
            return localOutdated
        }
        Log.i(logAppointments, localAndRemoteSynchronized)
        return localAndRemoteSynchronized
    }
}