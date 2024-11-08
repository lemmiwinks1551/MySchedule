package com.example.projectnailsschedule.presentation.main

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
import com.example.projectnailsschedule.domain.usecase.apiUC.serverSyncUC.GetNotSyncAppointmentsUseCase
import com.example.projectnailsschedule.domain.usecase.apiUC.serverSyncUC.InsertAppointmentDtoUseCase
import com.example.projectnailsschedule.domain.usecase.apiUC.serverSyncUC.PostAppointmentUseCase
import com.example.projectnailsschedule.domain.usecase.apiUC.serverSyncUC.UpdateAppointmentDtoUseCase
import com.example.projectnailsschedule.domain.usecase.appointmentUC.GetAllScheduleDbUseCase
import com.example.projectnailsschedule.domain.usecase.settingsUC.GetUserThemeUseCase
import com.example.projectnailsschedule.util.Util
import dagger.hilt.android.lifecycle.HiltViewModel
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

    private var postAppointmentUseCase: PostAppointmentUseCase,
    private var deleteRemoteAppointmentUseCase: DeleteRemoteAppointmentUseCase
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

    suspend fun mergeDatabase() {
        // Если пользователь не залогинился - выходим
        getUserInfoApi() ?: return

        // Получаем все записи, которые пользоватль создал локально
        val localScheduleDb = getAllScheduleDbUseCase.execute()

        // Получаем все записи добавленые в БД для синхронизации
        val scheduleSyncDb = getScheduleSyncDb()

        // Получаем уникальные id в базе данных для синхронизации с сервером
        val syncDbIds = scheduleSyncDb.map { it.localAppointmentId }.toSet()

        // Добавляем все записи, которых еще нет в БД для синхронизации
        for (localAppointment in localScheduleDb) {
            if (localAppointment._id !in syncDbIds) {

                // если id локальной записи нет в scheduleSyncDb - добавляем её в ScheduleSyncDb
                val appointmentDto = AppointmentDto(
                    syncUUID = UUID.randomUUID().toString(),
                    localAppointmentId = localAppointment._id!!,
                    userName = UserInfoDtoManager.getUserDto()!!.username,
                    syncTimestamp = Util().generateTimestamp(),
                    syncStatus = "NotSynchronized",
                    appointmentDate = localAppointment.date,
                    appointmentTime = localAppointment.time,
                    appointmentNotes = localAppointment.notes,

                    clientId = localAppointment.clientId.toString(),
                    clientName = localAppointment.name,
                    clientPhone = localAppointment.phone,
                    clientTelegram = localAppointment.telegram,
                    clientInstagram = localAppointment.instagram,
                    clientVk = localAppointment.vk,
                    clientWhatsapp = localAppointment.whatsapp,
                    clientNotes = localAppointment.clientNotes,
                    clientPhoto = localAppointment.photo,

                    procedureId = null,
                    procedureName = localAppointment.procedure,
                    procedurePrice = localAppointment.procedurePrice,
                    procedureNotes = localAppointment.procedureNotes
                )

                insertAppointmentDtoUseCase.execute(appointmentDto)
            }
        }

        syncLocalDbWithRemoteDb()
    }

    private suspend fun syncLocalDbWithRemoteDb() {
        val allNotSyncAppointments = getNotSyncAppointmentsUseCase.execute()

        for (notSyncAppointment in allNotSyncAppointments) {
            val result = postAppointmentUseCase.execute(notSyncAppointment, getJwt.execute()!!)
            if (result == "200") {
                // Устанавливает статус Synchronized в БД ScheduleRemoteDb
                notSyncAppointment.syncStatus = "Synchronized"
                updateAppointmentDtoUseCase.execute(notSyncAppointment)
            }
        }

        val deletedAppointments = getDeletedAppointmentsUseCase.execute()

        for (deletedAppointment in deletedAppointments) {
            val result = deleteRemoteAppointmentUseCase.execute(deletedAppointment, getJwt.execute()!!)
            if (result == "200") {
                // Удаляем запись из локальной БД для синхронизации
                deleteAppointmentDtoUseCase.execute(deletedAppointment)
            }
        }
    }
}