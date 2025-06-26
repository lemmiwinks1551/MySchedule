package com.example.projectnailsschedule.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectnailsschedule.domain.models.FaqModel
import com.example.projectnailsschedule.domain.models.dto.UserInfoDto
import com.example.projectnailsschedule.domain.models.dto.UserInfoDtoManager
import com.example.projectnailsschedule.domain.usecase.account.GetJwt
import com.example.projectnailsschedule.domain.usecase.account.GetUserInfoApiUseCase
import com.example.projectnailsschedule.domain.usecase.apiUC.GetFaqUseCase
import com.example.projectnailsschedule.domain.usecase.apiUC.serverSyncUC.DisableSyncUseCase
import com.example.projectnailsschedule.domain.usecase.apiUC.serverSyncUC.EnableSyncUseCase
import com.example.projectnailsschedule.domain.usecase.rustore.CheckRuStoreLoginStatus
import com.example.projectnailsschedule.domain.usecase.rustore.GetPurchasesUseCase
import com.example.projectnailsschedule.domain.usecase.settingsUC.GetLanguageUseCase
import com.example.projectnailsschedule.domain.usecase.settingsUC.GetThemeUseCase
import com.example.projectnailsschedule.domain.usecase.settingsUC.GetUserThemeUseCase
import com.example.projectnailsschedule.domain.usecase.settingsUC.SetDarkThemeUseCase
import com.example.projectnailsschedule.domain.usecase.settingsUC.SetLanguageUseCase
import com.example.projectnailsschedule.domain.usecase.settingsUC.SetLightThemeUseCase
import com.example.projectnailsschedule.domain.usecase.settingsUC.SetUserThemeUseCase
import com.example.projectnailsschedule.domain.usecase.util.RestartAppUseCase
import com.example.projectnailsschedule.domain.usecase.util.UpdateUserDataUseCase
import com.example.projectnailsschedule.util.Util
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val setLightThemeUseCase: SetLightThemeUseCase,
    private val setDarkThemeUseCase: SetDarkThemeUseCase,
    private val getThemeUseCase: GetThemeUseCase,
    private val getLanguageUseCase: GetLanguageUseCase,
    private val setLanguageUseCase: SetLanguageUseCase,
    private val setUserThemeUseCase: SetUserThemeUseCase,
    private val getUserThemeUseCase: GetUserThemeUseCase,
    private var restartAppUseCase: RestartAppUseCase,
    private var updateUserDataUseCase: UpdateUserDataUseCase,
    private var getFaqUseCase: GetFaqUseCase,

    private var getUserInfoApi: GetUserInfoApiUseCase,
    private var getJwt: GetJwt,
    private var enableSyncUseCase: EnableSyncUseCase,
    private var disableSyncUseCase: DisableSyncUseCase,

    private val getRuStoreLoginStatus: CheckRuStoreLoginStatus,
    private var getPurchasesUseCase: GetPurchasesUseCase,
) : ViewModel() {

    init {
        //loadTheme()
    }

    var darkThemeOn: Boolean? = null

    fun setLightTheme() {
        setLightThemeUseCase.execute()
        darkThemeOn = false
    }

    fun setDarkTheme() {
        setDarkThemeUseCase.execute()
        darkThemeOn = true
    }

    private fun loadTheme() {
        darkThemeOn = getThemeUseCase.execute()
    }

    fun setLanguage(language: String) {
    }

    fun setUserTheme(theme: String) {
        setUserThemeUseCase.execute(theme)
    }

    fun getUserTheme(): String {
        return getUserThemeUseCase.execute()
    }

    fun restartApp() {
        restartAppUseCase.execute()
    }

    fun updateUserData(event: String) {
        updateUserDataUseCase.execute(event)
    }

    suspend fun getFaq(): List<FaqModel> {
        return getFaqUseCase.execute()
    }

    suspend fun getUserInfoApi(): UserInfoDto? {
        val jwt = getJwt.execute() ?: return null
        val username = Util().extractUsernameFromJwt(jwt) ?: return null
        val userInfo = getUserInfoApi.execute(username, jwt)?.body() ?: return null

        UserInfoDtoManager.setUserDto(userInfo)
        return userInfo
    }

    suspend fun enableSync(): Boolean {
        return try {
            enableSyncUseCase.execute(getUserInfoApi()!!, getJwt.execute()!!)
        } catch (e: Exception) {
            false
        }
    }

    suspend fun disableSync(): Boolean {
        return try {
            disableSyncUseCase.execute(getUserInfoApi()!!, getJwt.execute()!!)
        } catch (e: Exception) {
            false
        }
    }

    suspend fun isAuthorizedRuStore(): Boolean {
        return viewModelScope.async {
            withContext(Dispatchers.IO) {
                getRuStoreLoginStatus.execute().await().authorized
            }
        }.await()
    }

    suspend fun isSubscribed(): Boolean {
        // Проверяем, куплена ли подписка у пользователя
        return getPurchasesUseCase.execute().fold(
            onSuccess = {
                it.isNotEmpty()
            },
            onFailure = {
                false
            }
        )
    }
}