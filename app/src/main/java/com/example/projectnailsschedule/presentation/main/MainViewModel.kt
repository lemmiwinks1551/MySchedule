package com.example.projectnailsschedule.presentation.main

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.projectnailsschedule.domain.models.UserDataManager
import com.example.projectnailsschedule.domain.models.dto.AppointmentDto
import com.example.projectnailsschedule.domain.models.dto.UserInfoDto
import com.example.projectnailsschedule.domain.models.dto.UserInfoDtoManager
import com.example.projectnailsschedule.domain.usecase.account.GetJwt
import com.example.projectnailsschedule.domain.usecase.account.GetUserInfoApiUseCase
import com.example.projectnailsschedule.domain.usecase.apiUC.SendUserDataUseCase
import com.example.projectnailsschedule.domain.usecase.apiUC.serverSyncUC.DeleteAppointmentDtoUseCase
import com.example.projectnailsschedule.domain.usecase.apiUC.serverSyncUC.DeleteRemoteAppointmentUseCase
import com.example.projectnailsschedule.domain.usecase.apiUC.serverSyncUC.GetAllScheduleSyncDb
import com.example.projectnailsschedule.domain.usecase.apiUC.serverSyncUC.GetDeletedAppointmentsUseCase
import com.example.projectnailsschedule.domain.usecase.apiUC.serverSyncUC.GetMaxAppointmentTimestampUseCase
import com.example.projectnailsschedule.domain.usecase.apiUC.serverSyncUC.GetNotSyncAppointmentsUseCase
import com.example.projectnailsschedule.domain.usecase.apiUC.serverSyncUC.GetUserRemoteAppointmentsUseCase
import com.example.projectnailsschedule.domain.usecase.apiUC.serverSyncUC.InsertAppointmentDtoUseCase
import com.example.projectnailsschedule.domain.usecase.apiUC.serverSyncUC.PostAppointmentUseCase
import com.example.projectnailsschedule.domain.usecase.apiUC.serverSyncUC.UpdateAppointmentDtoUseCase
import com.example.projectnailsschedule.domain.usecase.appointmentUC.GetAllScheduleDbUseCase
import com.example.projectnailsschedule.domain.usecase.settingsUC.GetUserThemeUseCase
import com.example.projectnailsschedule.util.Util
import dagger.hilt.android.lifecycle.HiltViewModel
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getUserThemeUseCase: GetUserThemeUseCase,
    private val sendUserDataUseCase: SendUserDataUseCase,
    private var getUserInfoApi: GetUserInfoApiUseCase,
    private var getJwt: GetJwt,

    private var getAllScheduleDbUseCase: GetAllScheduleDbUseCase,

    private var getAllScheduleSyncDb: GetAllScheduleSyncDb,

    private var insertAppointmentDtoUseCase: InsertAppointmentDtoUseCase,
    private var updateAppointmentDtoUseCase: UpdateAppointmentDtoUseCase,
    private var deleteAppointmentDtoUseCase: DeleteAppointmentDtoUseCase,

    private var getNotSyncAppointmentsUseCase: GetNotSyncAppointmentsUseCase,
    private var getDeletedAppointmentsUseCase: GetDeletedAppointmentsUseCase,
    private var getMaxAppointmentTimestampUseCase: GetMaxAppointmentTimestampUseCase,

    private var postAppointmentUseCase: PostAppointmentUseCase,
    private var deleteRemoteAppointmentUseCase: DeleteRemoteAppointmentUseCase,
    private var getUserRemoteAppointmentsUseCase: GetUserRemoteAppointmentsUseCase
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

    private suspend fun getScheduleSyncDb(): List<AppointmentDto> {
        return getAllScheduleSyncDb.execute()
    }

    suspend fun syncRemoteToLocal() {
        // Получаем юзера, если пользователь не залогинился - выходим
        val user = getUserInfoApi() ?: return

        // Получаем самую позднюю локальную запись по временной метке, если не нашел запись - выходим
        val lastTimestamp = getMaxAppointmentTimestampUseCase.execute() ?: return

        // Получаем записи юзера на сервере (нужно как-то сокращать количество записей для выгрузки
        // а то получится очень много трафика будет скушано (по временной метке как-то ...)
        // TODO: в бд обрезаются миллисекунды 
        Log.i(
            log,
            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault()).format(lastTimestamp)
        )

        val userRemoteAppointments = getUserRemoteAppointmentsUseCase.execute(user)
    }

    // Отправляет записи из локальной базы данных на сервер и обрабатывает статус синхронизации
    suspend fun postLocalDbToRemote() {
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