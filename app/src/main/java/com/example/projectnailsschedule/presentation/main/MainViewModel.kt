package com.example.projectnailsschedule.presentation.main

import androidx.lifecycle.ViewModel
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
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getUserThemeUseCase: GetUserThemeUseCase,
    private val sendUserDataUseCase: SendUserDataUseCase,
    private var getUserInfoApi: GetUserInfoApiUseCase,
    private var getJwt: GetJwt,
) : ViewModel() {

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
}