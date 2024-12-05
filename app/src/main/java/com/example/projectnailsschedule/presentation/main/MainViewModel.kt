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
import com.example.projectnailsschedule.domain.usecase.apiUC.localSyncDbUC.GetCountSyncDbUseCase
import com.example.projectnailsschedule.domain.usecase.apiUC.localSyncDbUC.GetDeletedAppointmentsUseCase
import com.example.projectnailsschedule.domain.usecase.apiUC.localSyncDbUC.GetNotSyncAppointmentsUseCase
import com.example.projectnailsschedule.domain.usecase.apiUC.localSyncDbUC.GetUserLastLocalAppointmentTimestamp
import com.example.projectnailsschedule.domain.usecase.apiUC.serverSyncUC.DeleteRemoteAppointmentUseCase
import com.example.projectnailsschedule.domain.usecase.apiUC.serverSyncUC.GetLastRemoteAppointmentTimestamp
import com.example.projectnailsschedule.domain.usecase.apiUC.serverSyncUC.GetUserRemoteAppointmentsAfterTimestampUseCase
import com.example.projectnailsschedule.domain.usecase.apiUC.serverSyncUC.GetUserRemoteDbCountUseCase
import com.example.projectnailsschedule.domain.usecase.apiUC.serverSyncUC.PostAppointmentUseCase
import com.example.projectnailsschedule.domain.usecase.apiUC.serverSyncUC.serverCalendarColorApiUC.DeleteRemoteCalendarDateUseCase
import com.example.projectnailsschedule.domain.usecase.apiUC.serverSyncUC.serverCalendarColorApiUC.GetAfterTimestampCalendarDateUseCase
import com.example.projectnailsschedule.domain.usecase.apiUC.serverSyncUC.serverCalendarColorApiUC.GetLastRemoteTimestampCalendarDateUseCase
import com.example.projectnailsschedule.domain.usecase.apiUC.serverSyncUC.serverCalendarColorApiUC.GetRemoteCountCCalendarDateUseCase
import com.example.projectnailsschedule.domain.usecase.apiUC.serverSyncUC.serverCalendarColorApiUC.PostCalendarDateUseCase
import com.example.projectnailsschedule.domain.usecase.appointmentUC.DeleteAppointmentUseCase
import com.example.projectnailsschedule.domain.usecase.appointmentUC.InsertAppointmentUseCase
import com.example.projectnailsschedule.domain.usecase.appointmentUC.UpdateAppointmentUseCase
import com.example.projectnailsschedule.domain.usecase.calendarUC.DeleteCalendarDateUseCase
import com.example.projectnailsschedule.domain.usecase.calendarUC.GetBySyncUuidCalendarDateUseCase
import com.example.projectnailsschedule.domain.usecase.calendarUC.GetCountCalendarDateUseCase
import com.example.projectnailsschedule.domain.usecase.calendarUC.GetDeletedCalendarDateUseCase
import com.example.projectnailsschedule.domain.usecase.calendarUC.GetNotSyncCalendarDateUseCase
import com.example.projectnailsschedule.domain.usecase.calendarUC.GetUserLastLocalCalendarDateTimestamp
import com.example.projectnailsschedule.domain.usecase.calendarUC.InsertCalendarDateUseCase
import com.example.projectnailsschedule.domain.usecase.calendarUC.UpdateCalendarDateUseCase
import com.example.projectnailsschedule.domain.usecase.settingsUC.GetUserThemeUseCase
import com.example.projectnailsschedule.util.Util
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getUserThemeUseCase: GetUserThemeUseCase,
    private val sendUserDataUseCase: SendUserDataUseCase,
    private var getUserInfoApi: GetUserInfoApiUseCase,
    private var getJwt: GetJwt,

    // LocalDb
    private var insertAppointmentUseCase: InsertAppointmentUseCase,
    private val updateAppointmentUseCase: UpdateAppointmentUseCase,
    private var deleteAppointmentUseCase: DeleteAppointmentUseCase,

    // Db for sync
    private var getNotSyncAppointmentsUseCase: GetNotSyncAppointmentsUseCase,
    private var getDeletedAppointmentsUseCase: GetDeletedAppointmentsUseCase,
    private var getUserLastLocalAppointmentTimestamp: GetUserLastLocalAppointmentTimestamp,
    private var getGetBySyncUuidUseCase: GetBySyncUuidUseCase,
    private var getCountSyncDbUseCase: GetCountSyncDbUseCase,

    // API
    private var postAppointmentUseCase: PostAppointmentUseCase,
    private var deleteRemoteAppointmentUseCase: DeleteRemoteAppointmentUseCase,
    private var getUserLastRemoteAppointmentTimestamp: GetLastRemoteAppointmentTimestamp,
    private var getUserRemoteAppointmentsAfterTimestampUseCase: GetUserRemoteAppointmentsAfterTimestampUseCase,
    private var getUserRemoteDbCountUseCase: GetUserRemoteDbCountUseCase,

    // Calendar color API
    private var postRemoteCalendarDateUseCase: PostCalendarDateUseCase,
    private var deleteRemoteCalendarDateUseCase: DeleteRemoteCalendarDateUseCase,
    private var getLastRemoteTimestampCalendarDateUseCase: GetLastRemoteTimestampCalendarDateUseCase,
    private var getRemoteAfterTimestampCalendarDateUseCase: GetAfterTimestampCalendarDateUseCase,
    private var getRemoteCountCCalendarDateUseCase: GetRemoteCountCCalendarDateUseCase,

    // Calendar color local db:
    private var insertCalendarDateUseCase: InsertCalendarDateUseCase,
    private var deleteCalendarDateUseCase: DeleteCalendarDateUseCase,
    private var updateCalendarDateUseCase: UpdateCalendarDateUseCase,
    private var getNotSyncCalendarDateUseCase: GetNotSyncCalendarDateUseCase,
    private var getDeletedCalendarDateUseCase: GetDeletedCalendarDateUseCase,
    private var getUserLastLocalCalendarDateTimestamp: GetUserLastLocalCalendarDateTimestamp,
    private var getBySyncUuidCalendarDateUseCase: GetBySyncUuidCalendarDateUseCase,
    private var getCountCalendarDateUseCase: GetCountCalendarDateUseCase
) : ViewModel() {
    private val log = "SyncAppointments"
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

    //////////// Appointments
    suspend fun synchronizationCheck() {
        // Получаем юзера, если пользователь не залогинился - выходим
        val user = getUserInfoApi()

        if (user == null) {
            // Если пользователь не залогинился
            // устанавливаем статус false и выходим
            syncStatus.postValue(false)
            return
        }

        if (user.syncEnabled == false) {
            // Если у пользователя установлен syncEnabled == false
            // устанавливаем статус false и выходим
            syncStatus.postValue(false)
            return
        }

        syncStatus.postValue(true)

        // Получаем временные метки последней записи локально и удаленно
        val lastLocalTimestamp = getUserLastLocalAppointmentTimestamp.execute()
        val lastRemoteTimestamp =
            getUserLastRemoteAppointmentTimestamp.execute(user, getJwt.execute()!!)

        // Получаем общее количество записей в БД для синхронизации
        val localAppointmentsCount = getCountSyncDbUseCase.execute()

        // Получаем общее количество записей в удаленной БД
        val remoteAppointmentsCount = getUserRemoteDbCountUseCase.execute(getJwt.execute()!!)

        // TODO: Если с одного устройства 2 пользоватля зайдут - проблема
        // TODO: кэша много скапливается - проблема. Кэшируется код?

        if (lastLocalTimestamp == null && lastRemoteTimestamp != null) {
            Log.i(log, "Локальные данные не найдены - получаем данные с сервера")
            pullRemoteToLocalDb(Date(0).time)
        }

        if (localAppointmentsCount < remoteAppointmentsCount!!) {
            Log.i(log, "Локальных данных меньше, чем на сервере - получаем данные с сервера")
            Log.i(
                log,
                "Локальных данных $localAppointmentsCount - удаленных данных $remoteAppointmentsCount"
            )
            pullRemoteToLocalDb(Date(0).time)
        }

        if (localAppointmentsCount > remoteAppointmentsCount) {
            Log.i(log, "Локальных данных больше, чем на сервере - отправляем данные на сервер")
            Log.i(
                log,
                "Локальных данных $localAppointmentsCount - удаленных данных $remoteAppointmentsCount"
            )
            push()
        }

        if (lastLocalTimestamp != null && lastRemoteTimestamp == null) {
            Log.i(log, "Удаленные данные устарели - отправляем данные на сервер")
            push()
        }

        if (lastLocalTimestamp == null && lastRemoteTimestamp == null) {
            Log.i(log, "Данные локально и удаленно не найдены")
        }

        if (lastLocalTimestamp != null && lastRemoteTimestamp != null && lastLocalTimestamp == lastRemoteTimestamp) {
            Log.i(log, "Данные синхронизированы")
        }

        if (lastLocalTimestamp != null && lastRemoteTimestamp != null && lastLocalTimestamp > lastRemoteTimestamp
        ) {
            Log.i(log, "Локальное изменение позднее - отправляем данные на сервер")
            push()
        }

        if (lastLocalTimestamp != null && lastRemoteTimestamp != null && lastLocalTimestamp <
            lastRemoteTimestamp
        ) {
            Log.i(log, "Локальные данные устарели - получаем данные с сервера")
            Log.i(
                log,
                "Последнее локальное обновление: ${Date(lastLocalTimestamp)} " +
                        "Последнее удаленное обновление: ${Date(lastRemoteTimestamp)} "
            )
            pullRemoteToLocalDb(lastLocalTimestamp)
        }
    }

    private suspend fun push() {
        Log.i(log, "Отправляем данные на удаленную БД")

        // Получаем информацию о текущем пользователе
        val user = getUserInfoApi() ?: return

        // Извлекаем записи, которые еще не были синхронизированы
        val notSyncedAppointments = getNotSyncAppointmentsUseCase.execute()

        // Устанавливаем имя пользователя для записей, у которых оно отсутствует
        setUsernameAppointments(notSyncedAppointments, user)

        // Отправляем несинхронизированные записи на сервер
        for (appointment in notSyncedAppointments) {
            val result = postAppointmentUseCase.execute(appointment, getJwt.execute()!!)

            if (result == "200") {
                // Обновляем статус синхронизации для успешно отправленных записей
                appointment.syncStatus = "Synchronized"
                updateAppointmentUseCase.execute(appointment)
            }
        }

        // Обрабатываем удаленные записи, чтобы синхронизировать их с сервером
        val deletedAppointments = getDeletedAppointmentsUseCase.execute()
        for (appointment in deletedAppointments) {
            val result = deleteRemoteAppointmentUseCase.execute(appointment, getJwt.execute()!!)

            if (result == "200") {
                // Удаляем запись из локальной таблицы синхронизации, если она успешно удалена на сервере
                deleteAppointmentUseCase.execute(appointment)
            }
        }
    }

    private suspend fun pullRemoteToLocalDb(timestamp: Long) {
        Log.i(log, "Получаем данные с сервера")
        getUserInfoApi() ?: return
        val jwt = getJwt.execute() ?: return

        // Получаем все данные, у которых дата обновления > последней даты обновления в локальной БД
        val appointmentsToUpdate = getUserRemoteAppointmentsAfterTimestampUseCase.execute(
            jwt,
            timestamp
        )

        if (appointmentsToUpdate != null) {
            Log.i(log, "Данные для обновления получены")
            updateLocalDb(appointmentsToUpdate)
        } else {
            Log.i(log, "Данные для обновления не получены")
        }
    }

    private suspend fun updateLocalDb(appointments: List<AppointmentModelDb>) {
        Log.i(log, "Вносим данные в локальную БД. Записей для обработки: ${appointments.size}")

        for (updatedAppointment in appointments) {
            // проверяем, существует ли такая запись уже по uuid
            if (updatedAppointment.syncUUID == null) {
                continue
            }

            // ищем такую запись локально по syncUUID
            val localAppointment = getGetBySyncUuidUseCase.execute(updatedAppointment.syncUUID!!)

            // Если локальной записи такой нет - создаем её
            // или понимаем, что такая запись уже была удалена и выходим из обработки
            if (localAppointment == null) {
                Log.i(log, "Вносим данные в локальную БД (новая запись)")
                if (updatedAppointment.syncStatus!! == "NotSynchronized") {
                    // Обнуляем id, чтобы не затереть такую же запись
                    val newAppointment =
                        updatedAppointment.copy(_id = null, syncStatus = "Synchronized")

                    insertAppointmentUseCase.execute(newAppointment)
                }

                if (updatedAppointment.syncStatus!! == "DELETED") {
                    Log.i(log, "Вносим данные в локальную БД (запись удалена, выход)")
                    // TODO: не оптимальное поведение, приложение понимает, что на сервере есть
                    //  записи, которых нет локально, пробует обновиться, видит, что запись
                    //  удалена по статусу записи на сервере и пропускает обновление
                    continue
                }
            }

            // Если запись уже существует - сверяем даты, если дата "свежее" - заменяем
            if (localAppointment != null) {
                if (updatedAppointment.syncTimestamp!! > localAppointment.syncTimestamp!!) {
                    Log.i(log, "Вносим данные в локальную БД (обновляем запись)")

                    if (localAppointment.syncStatus == "DELETED") {
                        Log.i(
                            log, "Запись, которую необходимо обновить была удалена локально. " +
                                    "Обновление отменяется. Удаляем запись с сервера"
                        )
                        // Ставим Статус DELETED на сервер
                        val result = deleteRemoteAppointmentUseCase.execute(
                            updatedAppointment,
                            getJwt.execute()!!
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
                            log, "Запись с таким UUID найдена. " +
                                    "Дата обновления позднее. " +
                                    "Статус: NotSynchronized"
                        )

                        updatedAppointment.syncStatus = "Synchronized"
                        updateAppointmentUseCase.execute(updatedAppointment)
                    }

                    if (updatedAppointment.syncStatus!! == "DELETED") {
                        Log.i(log, "Статус на сервере DELETED, удаляем локальную запись")
                        deleteAppointmentUseCase.execute(localAppointment)
                    }
                }
            }
        }
    }

    // Устанавливает имя пользователя для записей без него перед синхронизацией
    private suspend fun setUsernameAppointments(
        notSyncedAppointments: List<AppointmentModelDb>,
        user: UserInfoDto
    ) {
        // Добавляем имя пользователя ко всем записям, у которых оно отсутствует
        for (appointment in notSyncedAppointments) {
            if (appointment.userName == null) {
                appointment.userName = user.username
                updateAppointmentUseCase.execute(appointment)
            }
        }
    }


    //////////// CalendarDate
    suspend fun synchronizationCheckCalendarDate() {
        // Получаем юзера, если пользователь не залогинился - выходим
        val user = getUserInfoApi()

        if (user == null) {
            // Если пользователь не залогинился
            // устанавливаем статус false и выходим
            syncStatus.postValue(false)
            return
        }

        if (user.syncEnabled == false) {
            // Если у пользователя установлен syncEnabled == false
            // устанавливаем статус false и выходим
            syncStatus.postValue(false)
            return
        }

        syncStatus.postValue(true)

        // Получаем временные метки последней записи локально и удаленно
        val lastLocalTimestamp = getUserLastLocalCalendarDateTimestamp.execute()
        val lastRemoteTimestamp =
            getLastRemoteTimestampCalendarDateUseCase.execute(user, getJwt.execute()!!)

        // Получаем общее количество записей в БД для синхронизации
        val localAppointmentsCount = getCountCalendarDateUseCase.execute()

        // Получаем общее количество записей в удаленной БД
        val remoteAppointmentsCount = getRemoteCountCCalendarDateUseCase.execute(getJwt.execute()!!)

        Log.i(logCalendar, "Последнее обновление и количество получены")

        if (lastLocalTimestamp == null && lastRemoteTimestamp != null) {
            Log.i(logCalendar, "Локальные данные не найдены - получаем данные с сервера")
            pullRemoteToLocalDbCalendarDate(Date(0).time)
        }

        if (localAppointmentsCount < remoteAppointmentsCount!!) {
            Log.i(
                logCalendar,
                "Локальных данных меньше, чем на сервере - получаем данные с сервера"
            )
            Log.i(
                logCalendar,
                "Локальных данных $localAppointmentsCount - удаленных данных $remoteAppointmentsCount"
            )
            pullRemoteToLocalDbCalendarDate(Date(0).time)
        }

        if (localAppointmentsCount > remoteAppointmentsCount) {
            Log.i(
                logCalendar,
                "Локальных данных больше, чем на сервере - отправляем данные на сервер"
            )
            Log.i(
                logCalendar,
                "Локальных данных $localAppointmentsCount - удаленных данных $remoteAppointmentsCount"
            )
            pushLocalDbToRemoteCalendarDate()
        }

        if (lastLocalTimestamp != null && lastRemoteTimestamp == null) {
            Log.i(logCalendar, "Удаленные данные устарели - отправляем данные на сервер")
            pushLocalDbToRemoteCalendarDate()
        }

        if (lastLocalTimestamp == null && lastRemoteTimestamp == null) {
            Log.i(logCalendar, "Данные локально и удаленно не найдены")
        }

        if (lastLocalTimestamp != null && lastRemoteTimestamp != null && lastLocalTimestamp == lastRemoteTimestamp) {
            Log.i(logCalendar, "Данные синхронизированы")
        }

        if (lastLocalTimestamp != null && lastRemoteTimestamp != null && lastLocalTimestamp > lastRemoteTimestamp
        ) {
            Log.i(logCalendar, "Локальное изменение позднее - отправляем данные на сервер")
            pushLocalDbToRemoteCalendarDate()
        }

        if (lastLocalTimestamp != null && lastRemoteTimestamp != null && lastLocalTimestamp < lastRemoteTimestamp
        ) {
            Log.i(logCalendar, "Локальные данные устарели - получаем данные с сервера")
            Log.i(
                logCalendar,
                "Последнее локальное обновление: $lastLocalTimestamp Последнее удаленное обновление: $lastRemoteTimestamp"
            )
            pullRemoteToLocalDbCalendarDate(lastLocalTimestamp)
        }
    }

    private suspend fun pushLocalDbToRemoteCalendarDate() {
        Log.i(logCalendar, "Отправляем данные на удаленную БД")

        // Получаем информацию о текущем пользователе
        val user = getUserInfoApi() ?: return

        // Извлекаем записи, которые еще не были синхронизированы
        val notSyncedAppointments = getNotSyncCalendarDateUseCase.execute()

        // Устанавливаем имя пользователя для записей, у которых оно отсутствует
        setUsernameForSyncRecordsCalendarDate(notSyncedAppointments, user)

        // Отправляем несинхронизированные записи на сервер
        for (calendarDateModelDb in notSyncedAppointments) {
            val result =
                postRemoteCalendarDateUseCase.execute(calendarDateModelDb, getJwt.execute()!!)

            if (result == "200") {
                // Обновляем статус синхронизации для успешно отправленных записей
                calendarDateModelDb.syncStatus = "Synchronized"
                updateCalendarDateUseCase.execute(calendarDateModelDb)
            }
        }

        // Обрабатываем удаленные записи
        val deletedCalendarDate = getDeletedCalendarDateUseCase.execute()

        for (calendarDateModelDb in deletedCalendarDate) {
            val result =
                deleteRemoteCalendarDateUseCase.execute(calendarDateModelDb, getJwt.execute()!!)

            if (result == "200") {
                // Удаляем запись из локальной таблицы синхронизации, если она успешно удалена на сервере
                deleteCalendarDateUseCase.execute(calendarDateModelDb)
            }
        }
    }

    private suspend fun pullRemoteToLocalDbCalendarDate(timestamp: Long) {
        Log.i(logCalendar, "Получаем данные с сервера")
        getUserInfoApi() ?: return
        val jwt = getJwt.execute() ?: return

        // Получаем все данные, у которых дата обновления > последней даты обновления в локальной БД
        val calendarDateDateToUpdate = getRemoteAfterTimestampCalendarDateUseCase.execute(
            jwt,
            timestamp
        )

        if (calendarDateDateToUpdate != null) {
            Log.i(logCalendar, "Данные для обновления получены")
            updateDbForSyncCalendarDate(calendarDateDateToUpdate)
        } else {
            Log.i(logCalendar, "Данные для обновления не получены")
        }
    }

    private suspend fun updateDbForSyncCalendarDate(calendarDateToUpdate: List<CalendarDateModelDb>) {
        Log.i(
            logCalendar,
            "Вносим данные в локальную БД. Записей для обработки: ${calendarDateToUpdate.size}"
        )

        for (updatedCalendarDate in calendarDateToUpdate) {
            // проверяем, существует ли такая запись уже по uuid
            val localCalendarDateModel =
                getBySyncUuidCalendarDateUseCase.execute(updatedCalendarDate.syncUUID!!)

            // Если локальной записи такой нет - создаем её
            // или понимаем, что такая запись уже была удалена и выходим из обработки
            if (localCalendarDateModel == null) {
                Log.i(logCalendar, "Вносим данные в локальную БД (новая запись)")
                if (updatedCalendarDate.syncStatus!! == "NotSynchronized") {
                    // Обнуляем id, чтобы не затереть такую же запись
                    val newLocalCalendarDate =
                        updatedCalendarDate.copy(_id = null, syncStatus = "Synchronized")

                    insertCalendarDateUseCase.execute(newLocalCalendarDate)
                }

                if (updatedCalendarDate.syncStatus!! == "DELETED") {
                    Log.i(logCalendar, "Вносим данные в локальную БД (запись удалена, выходим)")
                    continue
                }
            }

            // Если запись уже существует - сверяем даты, если дата "свежее" - заменяем
            if (localCalendarDateModel != null) {
                if (updatedCalendarDate.syncTimestamp!! > localCalendarDateModel.syncTimestamp!!) {
                    Log.i(logCalendar, "Вносим данные в локальную БД (обновляем запись)")

                    if (localCalendarDateModel.syncStatus == "DELETED") {
                        Log.i(
                            logCalendar,
                            "Запись, которую необходимо обновить была удалена локально. " +
                                    "Обновление отменяется. Удаляем запись с сервера"
                        )
                        // Ставим Статус DELETED на сервер
                        val result = deleteRemoteCalendarDateUseCase.execute(
                            updatedCalendarDate,
                            getJwt.execute()!!
                        )
                        if (result == "200") {
                            // Если Статус DELETED на сервере установлен - удаляем из локальной бд
                            deleteCalendarDateUseCase.execute(localCalendarDateModel)
                        }
                        return
                    }

                    if (updatedCalendarDate.syncStatus!! == "NotSynchronized") {
                        // Если запись была обновлена - обновляем её в БД
                        Log.i(
                            logCalendar,
                            "Статус на сервере NotSynchronized, обновляем локальную запись"
                        )
                        localCalendarDateModel.color = updatedCalendarDate.color
                        localCalendarDateModel.syncStatus = "Synchronized"
                        updateCalendarDateUseCase.execute(localCalendarDateModel)
                    }

                    if (updatedCalendarDate.syncStatus!! == "DELETED") {
                        Log.i(logCalendar, "Статус на сервере DELETED, удаляем локальную запись")
                        deleteCalendarDateUseCase.execute(localCalendarDateModel)
                    }
                }
            }
        }
    }

    // Устанавливает имя пользователя для записей без него перед синхронизацией
    private suspend fun setUsernameForSyncRecordsCalendarDate(
        notSyncedList: List<CalendarDateModelDb>,
        user: UserInfoDto
    ) {
        // Добавляем имя пользователя ко всем записям, у которых оно отсутствует
        for (calendarDateModel in notSyncedList) {
            if (calendarDateModel.userName == null) {
                calendarDateModel.userName = user.username
                updateCalendarDateUseCase.execute(calendarDateModel)
            }
        }
    }
}