package com.example.projectnailsschedule.presentation.main

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.projectnailsschedule.domain.models.AppointmentModelDb
import com.example.projectnailsschedule.domain.models.UserDataManager
import com.example.projectnailsschedule.domain.models.dto.AppointmentDto
import com.example.projectnailsschedule.domain.models.dto.UserInfoDto
import com.example.projectnailsschedule.domain.models.dto.UserInfoDtoManager
import com.example.projectnailsschedule.domain.usecase.account.GetJwt
import com.example.projectnailsschedule.domain.usecase.account.GetUserInfoApiUseCase
import com.example.projectnailsschedule.domain.usecase.apiUC.SendUserDataUseCase
import com.example.projectnailsschedule.domain.usecase.apiUC.localSyncDbUC.DeleteAppointmentDtoUseCase
import com.example.projectnailsschedule.domain.usecase.apiUC.localSyncDbUC.GetBySyncUuidUseCase
import com.example.projectnailsschedule.domain.usecase.apiUC.localSyncDbUC.GetCountSyncDbUseCase
import com.example.projectnailsschedule.domain.usecase.apiUC.localSyncDbUC.GetDeletedAppointmentsUseCase
import com.example.projectnailsschedule.domain.usecase.apiUC.localSyncDbUC.GetNotSyncAppointmentsUseCase
import com.example.projectnailsschedule.domain.usecase.apiUC.localSyncDbUC.GetUserLastLocalAppointmentTimestamp
import com.example.projectnailsschedule.domain.usecase.apiUC.localSyncDbUC.InsertAppointmentDtoUseCase
import com.example.projectnailsschedule.domain.usecase.apiUC.localSyncDbUC.UpdateAppointmentDtoUseCase
import com.example.projectnailsschedule.domain.usecase.apiUC.serverSyncUC.DeleteRemoteAppointmentUseCase
import com.example.projectnailsschedule.domain.usecase.apiUC.serverSyncUC.GetLastRemoteAppointmentTimestamp
import com.example.projectnailsschedule.domain.usecase.apiUC.serverSyncUC.GetUserRemoteAppointmentsAfterTimestampUseCase
import com.example.projectnailsschedule.domain.usecase.apiUC.serverSyncUC.GetUserRemoteDbCountUseCase
import com.example.projectnailsschedule.domain.usecase.apiUC.serverSyncUC.PostAppointmentUseCase
import com.example.projectnailsschedule.domain.usecase.appointmentUC.DeleteAppointmentUseCase
import com.example.projectnailsschedule.domain.usecase.appointmentUC.GetAppointmentById
import com.example.projectnailsschedule.domain.usecase.appointmentUC.InsertAppointmentUseCase
import com.example.projectnailsschedule.domain.usecase.appointmentUC.UpdateAppointmentUseCase
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
    private var insertAppointmentDtoUseCase: InsertAppointmentDtoUseCase,
    private var updateAppointmentDtoUseCase: UpdateAppointmentDtoUseCase,
    private var deleteAppointmentDtoUseCase: DeleteAppointmentDtoUseCase,
    private var getNotSyncAppointmentsUseCase: GetNotSyncAppointmentsUseCase,
    private var getDeletedAppointmentsUseCase: GetDeletedAppointmentsUseCase,
    private var getUserLastLocalAppointmentTimestamp: GetUserLastLocalAppointmentTimestamp,
    private var getGetBySyncUuidUseCase: GetBySyncUuidUseCase,
    private var getAppointmentById: GetAppointmentById,
    private var getCountSyncDbUseCase: GetCountSyncDbUseCase,

    // API
    private var postAppointmentUseCase: PostAppointmentUseCase,
    private var deleteRemoteAppointmentUseCase: DeleteRemoteAppointmentUseCase,
    private var getUserLastRemoteAppointmentTimestamp: GetLastRemoteAppointmentTimestamp,
    private var getUserRemoteAppointmentsAfterTimestampUseCase: GetUserRemoteAppointmentsAfterTimestampUseCase,
    private var getUserRemoteDbCountUseCase: GetUserRemoteDbCountUseCase
) : ViewModel() {
    private val log = "Sync"

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
            pushLocalDbToRemote()
        }

        if (lastLocalTimestamp != null && lastRemoteTimestamp == null) {
            Log.i(log, "Удаленные данные устарели - отправляем данные на сервер")
            pushLocalDbToRemote()
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
            pushLocalDbToRemote()
        }

        if (lastLocalTimestamp != null && lastRemoteTimestamp != null && lastLocalTimestamp <
            lastRemoteTimestamp
        ) {
            Log.i(log, "Локальные данные устарели - получаем данные с сервера")
            Log.i(
                log,
                "Последнее локальное обновление: $lastLocalTimestamp Последнее удаленное обновление: $lastRemoteTimestamp"
            )
            pullRemoteToLocalDb(lastLocalTimestamp)
        }
    }

    // Отправляет записи из локальной базы данных на сервер и обрабатывает статус синхронизации
    private suspend fun pushLocalDbToRemote() {
        Log.i(log, "Отправляем данные на удаленную БД")

        // Получаем информацию о текущем пользователе
        val user = getUserInfoApi() ?: return

        // Извлекаем записи, которые еще не были синхронизированы
        val notSyncedAppointments = getNotSyncAppointmentsUseCase.execute()

        // Устанавливаем имя пользователя для записей, у которых оно отсутствует
        setUsernameForSyncRecords(notSyncedAppointments, user)

        // Отправляем несинхронизированные записи на сервер
        for (appointment in notSyncedAppointments) {
            val result = postAppointmentUseCase.execute(appointment, getJwt.execute()!!)

            if (result == "200") {
                // Обновляем статус синхронизации для успешно отправленных записей
                appointment.syncStatus = "Synchronized"
                updateAppointmentDtoUseCase.execute(appointment)
            }
        }

        // Обрабатываем удаленные записи, чтобы синхронизировать их с сервером
        val deletedAppointments = getDeletedAppointmentsUseCase.execute()
        for (appointment in deletedAppointments) {
            val result = deleteRemoteAppointmentUseCase.execute(appointment, getJwt.execute()!!)

            if (result == "200") {
                // Удаляем запись из локальной таблицы синхронизации, если она успешно удалена на сервере
                deleteAppointmentDtoUseCase.execute(appointment)
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
            updateDbForSync(appointmentsToUpdate)
        } else {
            Log.i(log, "Данные для обновления не получены")
        }
    }

    private suspend fun updateDbForSync(appointments: List<AppointmentDto>) {
        Log.i(log, "Вносим данные в локальную БД. Записей для обработки: ${appointments.size}")

        for (updatedAppointment in appointments) {
            // проверяем, существует ли такая запись уже по uuid
            val localAppointment = getGetBySyncUuidUseCase.execute(updatedAppointment.syncUUID)

            // Если локальной записи такой нет - создаем её
            // или понимаем, что такая запись уже была удалена и выходим из обработки
            if (localAppointment == null) {
                Log.i(log, "Вносим данные в локальную БД (новая запись)")
                if (updatedAppointment.syncStatus!! == "NotSynchronized") {
                    val localId = insertInLocalDb(updatedAppointment)
                    // устанавливаем связь через локальный айди
                    updatedAppointment.localAppointmentId = localId

                    // обновляем в БД для синхронизации
                    insertAppointmentDtoUseCase.execute(updatedAppointment)
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
                if (updatedAppointment.syncTimestamp > localAppointment.syncTimestamp) {
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
                            // Если Статус DELETED на сервере установлен - удаляем из таблицы для синхронизации
                            deleteAppointmentDtoUseCase.execute(localAppointment)
                        }
                        return
                    }

                    if (updatedAppointment.syncStatus!! == "NotSynchronized") {
                        // Если запись была обновлена - обновляем её во всех БД
                        Log.i(log, "Статус на сервере NotSynchronized, обновляем локальную запись")

                        updatedAppointment.syncStatus = "Synchronized"
                        updatedAppointment.localAppointmentId =
                            localAppointment.localAppointmentId
                        insertAppointmentDtoUseCase.execute(updatedAppointment)
                        updateInLocalDb(updatedAppointment)
                    }

                    if (updatedAppointment.syncStatus!! == "DELETED") {
                        Log.i(log, "Статус на сервере DELETED, удаляем локальную запись")
                        // Если запись была удалена - удаляем её локально
                        // а в БД для синхронизации ставим статус DELETED
                        /*                        updatedAppointment.syncStatus = "DELETED"
                                                insertAppointmentDtoUseCase.execute(updatedAppointment)*/
                        // Удаляем её локально из БД для синхронизации и из локальной БД
                        deleteAppointmentDtoUseCase.execute(updatedAppointment)
                        val scheduleAppointment =
                            getAppointmentById.execute(localAppointment.localAppointmentId)
                        if (scheduleAppointment != null) {
                            deleteAppointmentUseCase.execute(scheduleAppointment)
                        }
                    }
                }
            }
        }
    }

    private suspend fun insertInLocalDb(appointmentDto: AppointmentDto): Long {
        // конвертируем в локальный формат
        // добавляем в БД и возвращаем id
        with(appointmentDto) {
            val appointmentModelDb = AppointmentModelDb(
                _id = null,
                date = appointmentDate,
                time = appointmentTime,
                notes = appointmentNotes,
                deleted = false,

                clientId = null,
                name = clientName,
                photo = clientPhoto,
                phone = clientPhone,
                telegram = clientTelegram,
                instagram = clientInstagram,
                vk = clientVk,
                whatsapp = clientWhatsapp,
                clientNotes = clientNotes,

                procedure = procedureName,
                procedurePrice = procedurePrice,
                procedureNotes = procedureNotes
            )
            Log.i(
                log,
                "Вносим данные в локальную БД. Новая запись создана SyncUUID: ${appointmentDto.syncUUID}"
            )
            return insertAppointmentUseCase.execute(appointmentModelDb)
        }
    }

    private suspend fun updateInLocalDb(appointmentDto: AppointmentDto) {
        // конвертируем в локальный формат и обновляем
        with(appointmentDto) {
            val appointmentModelDb = AppointmentModelDb(
                _id = appointmentDto.localAppointmentId,
                date = appointmentDate,
                time = appointmentTime,
                notes = appointmentNotes,
                deleted = false,

                clientId = null,
                name = clientName,
                photo = clientPhoto,
                phone = clientPhone,
                telegram = clientTelegram,
                instagram = clientInstagram,
                vk = clientVk,
                whatsapp = clientWhatsapp,
                clientNotes = clientNotes,

                procedure = procedureName,
                procedurePrice = procedurePrice,
                procedureNotes = procedureNotes
            )
            updateAppointmentUseCase.execute(appointmentModelDb)
            Log.i(log, "Запись обновлена SyncUUID: ${appointmentDto.syncUUID}")
        }
    }

    // Устанавливает имя пользователя для записей без него перед синхронизацией
    private suspend fun setUsernameForSyncRecords(
        notSyncedAppointments: List<AppointmentDto>,
        user: UserInfoDto
    ) {
        // Добавляем имя пользователя ко всем записям, у которых оно отсутствует
        for (appointment in notSyncedAppointments) {
            if (appointment.userName == null) {
                appointment.userName = user.username
                updateAppointmentDtoUseCase.execute(appointment)
            }
        }
    }
}