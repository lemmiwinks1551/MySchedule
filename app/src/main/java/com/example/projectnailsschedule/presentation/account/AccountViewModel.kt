package com.example.projectnailsschedule.presentation.account

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.auth0.android.jwt.JWT
import com.example.projectnailsschedule.domain.models.User
import com.example.projectnailsschedule.domain.models.dto.UserInfoDto
import com.example.projectnailsschedule.domain.models.dto.UserInfoDtoManager
import com.example.projectnailsschedule.domain.usecase.account.GetJwt
import com.example.projectnailsschedule.domain.usecase.account.GetUserInfoApiUseCase
import com.example.projectnailsschedule.domain.usecase.account.LoginUseCase
import com.example.projectnailsschedule.domain.usecase.account.LogoutUseCase
import com.example.projectnailsschedule.domain.usecase.account.RegistrationUseCase
import com.example.projectnailsschedule.domain.usecase.account.SendAccConfirmation
import com.example.projectnailsschedule.domain.usecase.account.SendPasswordResetConfirmation
import com.example.projectnailsschedule.domain.usecase.account.SetJwt
import com.example.projectnailsschedule.util.Util
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    private var loginUseCase: LoginUseCase,
    private var logoutUseCase: LogoutUseCase,
    private var registrationUseCase: RegistrationUseCase,
    private var sendAccConfirmation: SendAccConfirmation,
    private var sendPasswordResetConfirmation: SendPasswordResetConfirmation,
    private var setJwt: SetJwt,
    private var getJwt: GetJwt,
    private var getUserInfoApi: GetUserInfoApiUseCase,
) : ViewModel() {
    private val log = this::class.simpleName

    var user = MutableLiveData<UserInfoDto?>(null)
    val setUsername: (login: String) -> Unit = { }
    private val clearUser: () -> Unit = { user.postValue(null) }

    var requestDone = MutableLiveData(true)

    // Request status
    private val requestStarted: () -> Unit = { requestDone.postValue(false) }
    private val requestFinished: () -> Unit = { requestDone.postValue(true) }
    private val isRequestFree: () -> Boolean = { requestDone.value == true }

    // JWT getter/setter
    private var jwt: String?
        get() = getJwt.execute()
        set(value) {
            value?.let { setJwt.execute(it) }
        }

    init {
        // Получаем юзера из синглтона, если там null - пробуем получить его через api
        val currentUser = UserInfoDtoManager.getUserDto()
        if (currentUser != null) {
            user.postValue(currentUser)
        } else {
            CoroutineScope(Dispatchers.Main).launch {
                user.postValue(getUserInfoApi())
            }
        }
    }

    // Login

    suspend fun login(login: String, password: String): String? {
        if (!isRequestFree()) return null

        requestStarted()
        return try {
            // Выполнить запрос и установить JWT в Shared Pref
            val response = loginUseCase.execute(User(login, password))
            if (response?.code() == 403) {
                requestFinished()
                return "403"
            }
            if (response?.body() != null) {
                jwt = response.body()!!.token

                val userInfoDto = getUserInfoApi()

                if (userInfoDto != null) {
                    UserInfoDtoManager.setUserDto(userInfoDto)
                    user.postValue(userInfoDto)
                } else {
                    return null
                }

                requestFinished()
                return "Вход выполнен"
            } else {
                throw Exception()
            }
        } catch (e: Exception) {
            requestFinished()
            null
        }
    }

    suspend fun logout(): Boolean {
        if (!isRequestFree()) return false

        requestStarted()

        val logoutSuccessful: Boolean

        try {
            if (jwt != null) {
                logoutSuccessful = logoutUseCase.execute(jwt!!)
            } else {
                requestFinished()
                return false
            }

            if (!logoutSuccessful) {
                requestFinished()
                return false
            }

        } catch (e: Exception) {
            requestFinished()
            Log.e(log, e.toString())
        }

        requestFinished()

        jwt = ""

        UserInfoDtoManager.clearUserDto()

        user.postValue(null)

        return true
    }

    private fun extractUsernameFromJwt(jwt: String): String? {
        // Извлекаем логин из поля "sub"
        return try {
            JWT(jwt).getClaim("sub").asString().toString()
        } catch (e: Exception) {
            null
        }
    }

    // Registration + check

    suspend fun registration(username: String, email: String, password: String): String {
        return registrationUseCase.execute(username, email, password)
    }

    fun checkLogin(login: String): String? {
        if (login.length < 4 || login.length > 16) {
            return "Логин пользователя должен содержать от 4 до 16 символов"
        }

        if (isInvalidCharInLogin(login)) {
            return "Логин не должен начинаться или заканчиваться символами: " + "(., _, -). " +
                    "Содержать два и более специальных символов подряд или пробел."
        }

        return null
    }

    fun checkEmail(email: String): String? {
        if (!email.contains("@") ||
            email.isBlank() ||
            !email.contains(".")
        ) {
            return "Указан некорректный Email"
        }
        return null
    }

    fun checkPassword(password: String): String? {
        if (password.length < 8 || password.length > 16) {
            return "Пароль пользователя должен содержать от 8 до 16 символов"
        }
        return null
    }

    fun checkPasswordConfirm(password: String, passwordConfirm: String): String? {
        if (password != passwordConfirm) {
            return "Пароли не совпадают"
        }
        return null
    }

    private fun isInvalidCharInLogin(login: String): Boolean {
        val usernamePattern =
            "^(?!.*[._-]{2,})[a-zA-Zа-яА-ЯёЁ0-9][a-zA-Zа-яА-ЯёЁ0-9._-]*[a-zA-Zа-яА-ЯёЁ0-9]$"

        // Проверяем, соответствует ли строка регулярному выражению
        return !Regex(usernamePattern).matches(login)
    }

    // Confirmation

    suspend fun sendAccConfirmation(usernameOrEmail: String): String? {
        if (!isRequestFree()) return null
        requestStarted()

        var status: String? = null
        try {
            status = sendAccConfirmation.execute(usernameOrEmail)?.body()?.status
        } catch (e: Exception) {
            Log.e(log, e.message.toString())
        }

        requestFinished()
        return status
    }

    suspend fun sendPasswordResetConfirmation(): Boolean {
        return sendPasswordResetConfirmation.execute()
    }

    // Account

    private fun updateUser() {

    }

    private suspend fun getUserInfoApi(): UserInfoDto? {
        val jwt = getJwt.execute() ?: return null
        val username = Util().extractUsernameFromJwt(jwt) ?: return null
        val userInfo = getUserInfoApi.execute(username, jwt)?.body() ?: return null

        // Обновляем данные о пользователе
        UserInfoDtoManager.setUserDto(userInfo)
        return userInfo
    }
}