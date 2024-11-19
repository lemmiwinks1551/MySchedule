package com.example.projectnailsschedule.presentation.main

import android.util.Log
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
import com.example.projectnailsschedule.domain.usecase.apiUC.localSyncDbUC.GetDeletedAppointmentsUseCase
import com.example.projectnailsschedule.domain.usecase.apiUC.localSyncDbUC.GetNotSyncAppointmentsUseCase
import com.example.projectnailsschedule.domain.usecase.apiUC.localSyncDbUC.GetUserLastLocalAppointmentTimestamp
import com.example.projectnailsschedule.domain.usecase.apiUC.localSyncDbUC.InsertAppointmentDtoUseCase
import com.example.projectnailsschedule.domain.usecase.apiUC.localSyncDbUC.UpdateAppointmentDtoUseCase
import com.example.projectnailsschedule.domain.usecase.apiUC.serverSyncUC.DeleteRemoteAppointmentUseCase
import com.example.projectnailsschedule.domain.usecase.apiUC.serverSyncUC.GetLastRemoteAppointmentTimestamp
import com.example.projectnailsschedule.domain.usecase.apiUC.serverSyncUC.GetUserRemoteAppointmentsAfterTimestampUseCase
import com.example.projectnailsschedule.domain.usecase.apiUC.serverSyncUC.PostAppointmentUseCase
import com.example.projectnailsschedule.domain.usecase.appointmentUC.DeleteAppointmentUseCase
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

    // API
    private var postAppointmentUseCase: PostAppointmentUseCase,
    private var deleteRemoteAppointmentUseCase: DeleteRemoteAppointmentUseCase,
    private var getUserLastRemoteAppointmentTimestamp: GetLastRemoteAppointmentTimestamp,
    private var getUserRemoteAppointmentsAfterTimestampUseCase: GetUserRemoteAppointmentsAfterTimestampUseCase
) : ViewModel() {
    private val log = this::class.simpleName

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
        val user = getUserInfoApi() ?: return

        // Получаем временные метки последней записи локально и удаленно
        val lastLocalTimestamp = getUserLastLocalAppointmentTimestamp.execute()
        val lastRemoteTimestamp =
            getUserLastRemoteAppointmentTimestamp.execute(user, getJwt.execute()!!)

        when {
            // Локально данных нет, а удаленно есть - получаем данные
            lastLocalTimestamp == null && lastRemoteTimestamp != null -> {
                Log.i(log, "Локальные данные не найдены - получаем данные с сервера")
                pullRemoteToLocalDb(Date(0))
            }

            // Локально данные есть, а удаленно нет - отправляем данные
            lastLocalTimestamp != null && lastRemoteTimestamp == null -> {
                // TODO: Реализовать удаление записи с другого устройства
                Log.i(log, "Удаленные данные устарели - отправляем данные на сервер")
                pushLocalDbToRemote()
            }

            // Нет данных ни локально, ни удаленно - ничего не делаем
            lastLocalTimestamp == null && lastRemoteTimestamp == null -> {
                Log.i(log, "Данные локально и удаленно не найдены")
            }

            // Данные синхронизированы - выходим
            lastLocalTimestamp!!.time == lastRemoteTimestamp!!.time -> {
                Log.i(log, "Данные синхронизированы")
            }

            // Локальное изменение позднее - отправляем данные на сервер
            lastLocalTimestamp.after(lastRemoteTimestamp) -> {
                Log.i(log, "Удаленные данные устарели - отправляем данные на сервер")
                pushLocalDbToRemote()
            }

            // Удаленное изменение позднее - получаем данные с сервера
            lastLocalTimestamp.before(lastRemoteTimestamp) -> {
                Log.i(log, "Локальные данные устарели - получаем данные с сервера")
                pullRemoteToLocalDb(lastLocalTimestamp)
            }
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

    private suspend fun pullRemoteToLocalDb(timestamp: Date) {
        Log.i(log, "Получаем данные из удаленной БД")
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
        Log.i(log, "Вносим данные в локальную БД")

        for (updatedAppointment in appointments) {
            // проверяем, существует ли такая запись уже по uuid
            val localAppointment = getGetBySyncUuidUseCase.execute(updatedAppointment.syncUUID)

            // Если локальной записи такой нет - создаем её
            if (localAppointment == null) {
                Log.i(log, "Вносим данные в локальную БД (новая запись)")
                val localId = insertInLocalDb(updatedAppointment)

                // устанавливаем связь через локальный айди
                updatedAppointment.localAppointmentId = localId

                // обновляем в БД для синхронизации
                insertAppointmentDtoUseCase.execute(updatedAppointment)
                return
            }

            // Если запись уже существует - сверяем даты, если дата "свежее" - заменяем
            if (updatedAppointment.syncTimestamp.after(localAppointment.syncTimestamp)) {
                Log.i(log, "Вносим данные в локальную БД (обновляем запись)")
                updatedAppointment.syncStatus = "Synchronized"
                insertAppointmentDtoUseCase.execute(updatedAppointment)
                updateInLocalDb(updatedAppointment)
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
                clientId = null,
                name = clientName,
                time = appointmentTime,
                clientNotes = clientNotes,
                procedure = procedureName,
                procedurePrice = procedurePrice,
                procedureNotes = null,
                phone = clientPhone,
                vk = clientVk,
                telegram = clientTelegram,
                instagram = clientInstagram,
                whatsapp = clientWhatsapp,
                notes = clientNotes,
                photo = clientPhoto,
                deleted = false
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
                clientId = null,
                name = clientName,
                time = appointmentTime,
                clientNotes = clientNotes,
                procedure = procedureName,
                procedurePrice = procedurePrice,
                procedureNotes = null,
                phone = clientPhone,
                vk = clientVk,
                telegram = clientTelegram,
                instagram = clientInstagram,
                whatsapp = clientWhatsapp,
                notes = clientNotes,
                photo = clientPhoto,
                deleted = false
            )
            updateAppointmentUseCase.execute(appointmentModelDb)
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