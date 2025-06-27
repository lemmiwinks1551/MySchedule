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
import com.example.projectnailsschedule.domain.usecase.appointmentUC.GetOldUpdatedAppointmentsUseCase
import com.example.projectnailsschedule.domain.usecase.appointmentUC.InsertAppointmentUseCase
import com.example.projectnailsschedule.domain.usecase.appointmentUC.UpdateAppointmentUseCase
import com.example.projectnailsschedule.domain.usecase.calendarUC.DeleteCalendarDateUseCase
import com.example.projectnailsschedule.domain.usecase.calendarUC.GetBySyncUuidCalendarDateUseCase
import com.example.projectnailsschedule.domain.usecase.calendarUC.GetDeletedCalendarDateUseCase
import com.example.projectnailsschedule.domain.usecase.calendarUC.GetNotSyncCalendarDateUseCase
import com.example.projectnailsschedule.domain.usecase.calendarUC.GetOldUpdatedCalendarDateUseCase
import com.example.projectnailsschedule.domain.usecase.calendarUC.InsertCalendarDateUseCase
import com.example.projectnailsschedule.domain.usecase.calendarUC.SelectCalendarDateByDateUseCase
import com.example.projectnailsschedule.domain.usecase.calendarUC.UpdateCalendarDateUseCase
import com.example.projectnailsschedule.domain.usecase.premium.SetPremiumStatusUseCase
import com.example.projectnailsschedule.domain.usecase.rustore.CheckRuStoreLoginStatus
import com.example.projectnailsschedule.domain.usecase.rustore.GetPurchasesUseCase
import com.example.projectnailsschedule.domain.usecase.settingsUC.GetUserThemeUseCase
import com.example.projectnailsschedule.domain.usecase.sharedPref.GetAppointmentLastUpdateUseCase
import com.example.projectnailsschedule.domain.usecase.sharedPref.GetCalendarDateLastUpdateUseCase
import com.example.projectnailsschedule.domain.usecase.sharedPref.SetAppointmentLastUpdateUseCase
import com.example.projectnailsschedule.domain.usecase.sharedPref.SetCalendarLastUpdateUseCase
import com.example.projectnailsschedule.util.Util
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.suspendCancellableCoroutine
import ru.rustore.sdk.billingclient.RuStoreBillingClient
import ru.rustore.sdk.billingclient.model.purchase.PurchaseAvailabilityResult
import ru.rustore.sdk.billingclient.utils.pub.checkPurchasesAvailability
import java.util.Date
import javax.inject.Inject

const val localOutdated = "Локальные данные устарели!"
const val remoteOutdated = "Удаленные данные устарели!"
const val localAndRemoteSynchronized = "Данные синхронизированы"
const val synchronizedStatus = "Synchronized"
const val notSynchronizedStatus = "NotSynchronized"
const val deletedStatus = "DELETED"
const val okServerResponse = "200"
const val logAppointments = "SyncAppointments"
const val logCalendar = "SyncCalendar"

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
    private var getGetBySyncUuidUseCase: GetBySyncUuidUseCase,
    private var getOldUpdatedAppointmentsUseCase: GetOldUpdatedAppointmentsUseCase,

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
    private var getBySyncUuidCalendarDateUseCase: GetBySyncUuidCalendarDateUseCase,
    private val selectCalendarDateByDateUseCase: SelectCalendarDateByDateUseCase,
    private val getOldUpdatedCalendarDateUseCase: GetOldUpdatedCalendarDateUseCase,

    // CalendarDate API
    private var postRemoteCalendarDateUseCase: PostCalendarDateUseCase,
    private var deleteRemoteCalendarDateUseCase: DeleteRemoteCalendarDateUseCase,
    private var getLastRemoteTimestampCalendarDateUseCase: GetLastRemoteTimestampCalendarDateUseCase,
    private var getRemoteAfterTimestampCalendarDateUseCase: GetAfterTimestampCalendarDateUseCase,

    // Shared preferences
    private var getAppointmentLastUpdateUseCase: GetAppointmentLastUpdateUseCase,
    private var setAppointmentLastUpdateUseCase: SetAppointmentLastUpdateUseCase,
    private var getCalendarDateLastUpdateUseCase: GetCalendarDateLastUpdateUseCase,
    private var setCalendarLastUpdateUseCase: SetCalendarLastUpdateUseCase,

    // Billing SDK
    private val getPurchasesUseCase: GetPurchasesUseCase,
    private val checkRuStoreLoginStatus: CheckRuStoreLoginStatus,

    // Premium API
    private val setPremiumStatusUseCase: SetPremiumStatusUseCase
) : ViewModel() {
    val syncStatus = MutableLiveData(false)
    val menuStatus = MutableLiveData(false)

    private var appointmentsForPull = listOf<AppointmentModelDb>()
    private var calendarDatesForPull = listOf<CalendarDateModelDb>()

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
        val lastRemoteUpdateTime = getAppointmentLastUpdateUseCase.execute()
        val lastRemoteTimestamp =
            getUserLastRemoteAppointmentTimestamp.execute(user!!, jwt!!) ?: 0L

        if (localOutdated(lastRemoteUpdateTime, lastRemoteTimestamp, logAppointments)) {
            pullAppointments(lastRemoteUpdateTime, jwt)
        }

        if (getAppointmentsForPush()) {
            Log.i(logAppointments, remoteOutdated)
            pushAppointments(user, jwt)
        }
    }

    private suspend fun pushAppointments(user: UserInfoDto, jwt: String) {
        Log.i(logAppointments, "Выполняем метод pushAppointments()")
        /**
         *  1.  Выбирает все записи со статусом NotSynchronized
         *  1.1 Отправляет все записи на сервер
         *  1.2 Если, запись успешно обновлена на сервер (код 200) -> устанавливает статус Synchronized */

        /**
         *  2.  Выбирает все записи со статусом DELETED
         *  2.1 Отправляет все записи на сервер
         *  2.2 Если, запись успешно удалена на сервер (код 200) -> удаляет запись локально */

        // Устанавливаем имя пользователя для записей, у которых оно отсутствует
        setUsernameAppointments(appointmentsForPull, user)

        // Отправляем несинхронизированные и удаленные записи на сервер
        for (appointment in appointmentsForPull) {
            when (appointment.syncStatus) {
                notSynchronizedStatus -> {
                    val serverResponse = postAppointmentUseCase.execute(appointment, jwt)
                    if (serverResponse == okServerResponse) {
                        // Обновляем статус синхронизации локально, если запись успешно обновлена на сервере
                        appointment.syncStatus = synchronizedStatus
                        updateAppointmentUseCase.execute(appointment)
                    }
                }

                deletedStatus -> {
                    val serverResponse = deleteRemoteAppointmentUseCase.execute(appointment, jwt)
                    if (serverResponse == okServerResponse) {
                        // Удаляем запись локально, если она успешно удалена на сервере
                        deleteAppointmentUseCase.execute(appointment)
                    }
                }
            }
        }
    }

    private suspend fun pullAppointments(timestamp: Long, jwt: String) {
        Log.i(logAppointments, "Выполняем метод pullAppointments()")
        /**
         *  1.  Получает с сервера все записи, у которых дата ПОЗЖЕ указаной в timestamp
         *  2.  Выполняет обновление для каждой полученной записи */

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

        for (updatedAppointment in appointments) {
            // проверяем, существует ли такая запись уже по UUID
            val localAppointment =
                getGetBySyncUuidUseCase.execute(updatedAppointment.syncUUID!!)

            // Если локальной записи такой нет -> мы получили новую запись
            if (localAppointment == null) {
                if (updatedAppointment.syncStatus!! == notSynchronizedStatus) {
                    // Создаем новую запись
                    val newAppointment =
                        updatedAppointment.copy(_id = null, syncStatus = synchronizedStatus)

                    insertAppointmentUseCase.execute(newAppointment)
                    Log.i(logAppointments, "${newAppointment.syncUUID} создана")
                }

                if (updatedAppointment.syncStatus!! == deletedStatus) {
                    Log.i(
                        logAppointments,
                        "${updatedAppointment.syncStatus} - $deletedStatus, пропускаем"
                    )
                }
            }

            // Если локальной запись есть -> обновляем существующую запись
            if (localAppointment != null) {
                val localSyncUuid = localAppointment.syncUUID

                if (localAppointment.syncStatus.equals(deletedStatus)) {
                    Log.i(logAppointments, "$localSyncUuid уже удалена локально")
                    Log.i(logAppointments, "$localSyncUuid удаляем на сервере")

                    val result = deleteRemoteAppointmentUseCase.execute(
                        updatedAppointment, getJwt.execute()!!
                    )
                    if (result == okServerResponse) {
                        // Если Статус DELETED на сервере установлен - удаляем из локальной БД
                        deleteAppointmentUseCase.execute(localAppointment)
                    }
                }

                if (updatedAppointment.syncStatus.equals(notSynchronizedStatus)) {
                    // Если обновляемая запись более старая чем локальная по времени -
                    // отменяем обновление и переходим к следующей записи
                    if (updatedAppointment.syncTimestamp!! <= localAppointment.syncTimestamp!!) {
                        continue
                    }
                    updatedAppointment._id = localAppointment._id

                    // Заглушка, пока не настроена синхронизация клиентов - не обновляем поле clientID
                    updatedAppointment.clientId = localAppointment.clientId
                    updatedAppointment.syncStatus = synchronizedStatus
                    updateAppointmentUseCase.execute(updatedAppointment)
                    Log.i(logAppointments, "Запись $localSyncUuid обновлена")
                }

                if (updatedAppointment.syncStatus.equals(deletedStatus)) {
                    deleteAppointmentUseCase.execute(localAppointment)
                    Log.i(logAppointments, "Запись $localSyncUuid удалена")
                }

            }

            // Устанавливаем отметку последнего изменения в Shared preferences
            setAppointmentLastUpdateUseCase.execute(updatedAppointment.syncTimestamp!!)
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
        val lastLocalTimestamp = getCalendarDateLastUpdateUseCase.execute()
        val lastRemoteTimestamp =
            getLastRemoteTimestampCalendarDateUseCase.execute(user!!, jwt!!) ?: 0L

        if (localOutdated(lastLocalTimestamp, lastRemoteTimestamp, logCalendar)) {
            pullCalendarDate(lastLocalTimestamp, jwt)
        }

        if (getCalendarDatesForPush()) {
            Log.i(logCalendar, remoteOutdated)
            pushCalendarDate(user, jwt)
        }
    }

    private suspend fun pushCalendarDate(user: UserInfoDto, jwt: String) {
        Log.i(logCalendar, "Выполняем метод pushCalendarDate()")
        /**
         *  1.  Выбирает все записи со статусом NotSynchronized
         *  1.1 Отправляет все записи на сервер
         *  1.2 Если, запись успешно обновлена на сервер (код 200) -> устанавливает статус Synchronized */

        /**
         *  1.  Выбирает все записи со статусом DELETED
         *  2.1 Отправляет все записи на сервер
         *  2.2 Если, запись успешно удалена на сервер (код 200) -> удаляет запись локально */

        // Устанавливаем имя пользователя для записей, у которых оно отсутствует
        setUsernameCalendarDate(calendarDatesForPull, user)

        // Отправляем несинхронизированные и удаленные записи на сервер
        for (calendarDateModelDb in calendarDatesForPull) {
            when (calendarDateModelDb.syncStatus) {
                notSynchronizedStatus -> {
                    val serverResponse =
                        postRemoteCalendarDateUseCase.execute(calendarDateModelDb, jwt)
                    if (serverResponse == okServerResponse) {
                        // Обновляем статус синхронизации локально, если запись успешно обновлена на сервере
                        calendarDateModelDb.syncStatus = synchronizedStatus
                        updateCalendarDateUseCase.execute(calendarDateModelDb)
                    }
                }

                deletedStatus -> {
                    val serverResponse =
                        deleteRemoteCalendarDateUseCase.execute(calendarDateModelDb, jwt)
                    if (serverResponse == okServerResponse) {
                        // Удаляем запись локально, если она успешно удалена на сервере
                        deleteCalendarDateUseCase.execute(calendarDateModelDb)
                    }
                }
            }
        }
    }

    private suspend fun pullCalendarDate(timestamp: Long, jwt: String) {
        Log.i(logAppointments, "Выполняем метод pullCalendarDate()")
        /**
         *  1.  Получает с сервера все записи, у которых дата ПОЗЖЕ указаной в timestamp
         *  2.  Выполняет обновление для каждой полученной записи */

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
        Log.i(logCalendar, "Выполняем метод updateCalendarDateDb()")

        for (updatedCalendarDate in calendarDateToUpdate) {
            // Ищем запись сначала по UUID, затем по дате, если UUID не найден
            val localCalendarDate =
                getBySyncUuidCalendarDateUseCase.execute(updatedCalendarDate.syncUUID!!)
                    ?: selectCalendarDateByDateUseCase.execute(updatedCalendarDate.date!!)

            // Если локальной записи такой нет -> мы получили новую запись
            if (localCalendarDate == null) {
                if (updatedCalendarDate.syncStatus!! == notSynchronizedStatus) {
                    // Создаем новую запись
                    val newLocalCalendarDate =
                        updatedCalendarDate.copy(_id = null, syncStatus = synchronizedStatus)

                    insertCalendarDateUseCase.execute(newLocalCalendarDate)
                    Log.i(logCalendar, "${newLocalCalendarDate.syncUUID} создана")
                }

                if (updatedCalendarDate.syncStatus!! == deletedStatus) {
                    Log.i(
                        logCalendar,
                        "${updatedCalendarDate.syncUUID} = $deletedStatus, пропускаем"
                    )
                }
            } else {
                if (localCalendarDate.syncUUID != updatedCalendarDate.syncUUID) {
                    // Если запись была создана локально уже, но на сервере её нет - нужно ей присвоить
                    // существующий на сервере UUID по этой записи, чтобы не создавать копии на сервере в БД
                    updateCalendarDateUseCase.execute(localCalendarDate.copy(syncUUID = updatedCalendarDate.syncUUID))
                }

                // Если локальной запись есть -> обновляем существующую запись
                val localSyncUuid = localCalendarDate.syncUUID

                if (localCalendarDate.syncStatus.equals(deletedStatus)) {
                    Log.i(logCalendar, "$localSyncUuid уже удалена локально")
                    Log.i(logCalendar, "$localSyncUuid удаляем на сервере")

                    val result = deleteRemoteCalendarDateUseCase.execute(
                        updatedCalendarDate, getJwt.execute()!!
                    )
                    if (result == okServerResponse) {
                        // Если Статус DELETED на сервере установлен - удаляем из локальной бд
                        deleteCalendarDateUseCase.execute(localCalendarDate)
                    }
                }

                if (updatedCalendarDate.syncStatus.equals(notSynchronizedStatus)) {
                    if (updatedCalendarDate.syncTimestamp!! > localCalendarDate.syncTimestamp!!) {
                        val calendarDateForInput = updatedCalendarDate.copy(
                            _id = localCalendarDate._id,
                            syncStatus = synchronizedStatus
                        )
                        updateCalendarDateUseCase.execute(calendarDateForInput)
                        Log.i(logCalendar, "Запись $localSyncUuid обновлена")
                    }
                }

                if (updatedCalendarDate.syncStatus.equals(deletedStatus)) {
                    deleteCalendarDateUseCase.execute(localCalendarDate)
                    Log.i(logCalendar, "Запись $localSyncUuid удалена")
                }
            }

            // Устанавливаем отметку последнего изменения в Shared preferences
            setCalendarLastUpdateUseCase.execute(updatedCalendarDate.syncTimestamp!!)
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

    private suspend fun checkSyncConditions(user: UserInfoDto?, jwt: String?): Boolean {
        // --- если пользователь — бета-тестер, сразу разрешаем синхронизацию ---
        if (user?.betaTester == true) {
            Log.i("checkSyncConditions", "Пользователь — бета-тестер, синхронизация разрешена")
            syncStatus.postValue(true)
            return true
        }

        // --- сначала проверяем, можно ли вообще безопасно запрашивать покупки ---
        if (!canSafelyCheckPurchases()) {
            Log.i("checkSyncConditions", "Проверка подписки невозможна — RuStore недоступен")
            syncStatus.postValue(false)
            return false
        }

        // --- проверка входа в аккаунт и авторизации в RuStore ---
        if (user == null || jwt == null || !checkRuStoreLoginStatus.execute().await().authorized) {
            Log.i("checkSyncConditions", "Не выполнен вход в аккаунт или RuStore")
            syncStatus.postValue(false)
            return false
        }

        // --- всегда проверяем подписку и отправляем статус на сервер ---
        val hasPremium = getPurchasesUseCase.execute().fold(
            onSuccess = { purchases ->
                val result = purchases.isNotEmpty()
                setPremiumStatusUseCase.invoke(jwt, result)
                Log.i("checkSyncConditions", "Подписка: ${if (result) "куплена" else "не куплена"}")
                result
            },
            onFailure = {
                setPremiumStatusUseCase.invoke(jwt, false)
                Log.i("checkSyncConditions", "Не удалось получить данные о подписке: ${it.message}")
                false
            }
        )

        // --- теперь проверяем условия для включения синхронизации ---
        if (user.syncEnabled == false) {
            syncStatus.postValue(false)
            return false
        }

        if (!hasPremium) {
            syncStatus.postValue(false)
            return false
        }

        syncStatus.postValue(true)
        return true
    }

    private fun localOutdated(
        lastLocalTimestamp: Long,
        lastRemoteTimestamp: Long,
        log: String
    ): Boolean {
        // Проверка, устарели ли локальные данные
        return if (lastLocalTimestamp < lastRemoteTimestamp) {
            Log.i(log, localOutdated)
            Log.i(
                log,
                "Последнее локальное обновление: ${Date(lastLocalTimestamp)} "
                        + "Последнее удаленное обновление: ${Date(lastRemoteTimestamp)}"
            )
            true
        } else {
            false
        }
    }

    private suspend fun getAppointmentsForPush(): Boolean {
        // todo Добавить SQL-Запрос получения количества NotSynchronized и DELETED статусов
        //  для бОльшей производительности функции. Если их больше 0 то - выгружать уже список
        // Получаем записи, которые еще не были синхронизированы (syncStatus = "NotSynchronized")
        val notSyncedData = getNotSyncAppointmentsUseCase.execute()

        // Получаем записи, которые были удалены (syncStatus = "DELETED")
        val deletedData = getDeletedAppointmentsUseCase.execute()

        // Создаем общий лист несинхронизированных и удаленных записей
        appointmentsForPull = (notSyncedData + deletedData).sortedBy { it.syncTimestamp }

        return appointmentsForPull.isNotEmpty()
    }

    private suspend fun getCalendarDatesForPush(): Boolean {
        // todo Добавить SQL-Запрос получения количества NotSynchronized и DELETED статусов
        //  для бОльшей производительности функции. Если их больше 0 то - выгружать уже список
        // Получаем записи, которые еще не были синхронизированы (syncStatus = "NotSynchronized")
        val notSyncedData = getNotSyncCalendarDateUseCase.execute()

        // Получаем записи, которые были удалены (syncStatus = "DELETED")
        val deletedData = getDeletedCalendarDateUseCase.execute()

        // Создаем общий лист несинхронизированных и удаленных записей
        calendarDatesForPull = (notSyncedData + deletedData).sortedBy { it.syncTimestamp }

        return calendarDatesForPull.isNotEmpty()
    }

    suspend fun deleteOldDtData() {
        // Метод исправляет ошибку, когда приложение пытается отправить на сервер данные,
        // созданные до внедрения синхронизации с сервером
        // Метод очищает поля syncTimestamp и syncStatus у записей, у которых нет syncUUID
        val appointmentModelDbList = getOldUpdatedAppointmentsUseCase.execute()
        for (appointment in appointmentModelDbList) {
            appointment.syncTimestamp = null
            appointment.syncStatus = null
            updateAppointmentUseCase.execute(appointment)
        }

        val calendarDateModelDbList = getOldUpdatedCalendarDateUseCase.execute()
        for (calendarDate in calendarDateModelDbList) {
            calendarDate.syncTimestamp = null
            calendarDate.syncStatus = null
            updateCalendarDateUseCase.execute(calendarDate)
        }
    }

    private suspend fun canSafelyCheckPurchases(): Boolean {
        return suspendCancellableCoroutine { cont ->
            RuStoreBillingClient.checkPurchasesAvailability()
                .addOnSuccessListener { result ->
                    val isAvailable = result is PurchaseAvailabilityResult.Available
                    cont.resume(isAvailable) {}
                }
                .addOnFailureListener {
                    cont.resume(false) {}
                }
        }
    }
}