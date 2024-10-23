package com.example.projectnailsschedule.presentation.account

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.auth0.android.jwt.JWT
import com.example.projectnailsschedule.domain.models.User
import com.example.projectnailsschedule.domain.usecase.account.GetJwt
import com.example.projectnailsschedule.domain.usecase.account.LoginUseCase
import com.example.projectnailsschedule.domain.usecase.account.LogoutUseCase
import com.example.projectnailsschedule.domain.usecase.account.RegistrationUseCase
import com.example.projectnailsschedule.domain.usecase.account.SendAccConfirmation
import com.example.projectnailsschedule.domain.usecase.account.SendPasswordResetConfirmation
import com.example.projectnailsschedule.domain.usecase.account.SetJwt
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    private var loginUseCase: LoginUseCase,
    private var logoutUseCase: LogoutUseCase,
    private var registrationUseCase: RegistrationUseCase,
    private var sendAccConfirmation: SendAccConfirmation,
    private var sendPasswordResetConfirmation: SendPasswordResetConfirmation,
    private var setJwt: SetJwt,
    private var getJwt: GetJwt
) : ViewModel() {

    var user = MutableLiveData<User?>(null)
    var requestDone = MutableLiveData(true)
    private var jwt: String?

    // Статусы сетевого запроса
    val requestStarted: () -> Unit = { requestDone.postValue(false) }
    val requestFinished: () -> Unit = { requestDone.postValue(true) }

    // Работа с user
    val setUsername: (login: String) -> Unit = { user.postValue(User(it, null)) }
    val clearUser: () -> Unit = { user.postValue(null) }

    init {
        // Пробуем получить JWT из SharedPreference и установить username
        jwt = getJwt()
        getJwt()?.let { extractLoginFromJwt(it) }?.let { setUsername(it) }
    }

    suspend fun login(login: String, password: String): Boolean {
        if (!isRequestFree()) return false

        requestStarted()

        val jwt = loginUseCase.execute(User(login, password))

        requestFinished()

        if (jwt == null) return false

        // Установить в объект user его username
        extractLoginFromJwt(jwt)?.let { setUsername(it) }

        if (extractLoginFromJwt(jwt) == null) {
            return false
        }

        // Установить JWT в SharedPreference
        return setJwt.execute(jwt)
    }

    suspend fun logout(): Boolean {
        if (!isRequestFree()) return false

        requestStarted()

        val logoutSuccessful: Boolean


        if (jwt != null) {
            logoutSuccessful = logoutUseCase.execute(jwt!!)
            requestFinished()
        } else {
            requestFinished()
            return false
        }

        if (!logoutSuccessful) {
            return false
        } else {
            clearUser()
        }

        // Set JWT = null
        return setJwt.execute(null)
    }

    suspend fun registration(): Boolean {
        return registrationUseCase.execute()
    }

    suspend fun sendAccConfirmation(): Boolean {
        return sendAccConfirmation.execute()
    }

    suspend fun sendPasswordResetConfirmation(): Boolean {
        return sendPasswordResetConfirmation.execute()
    }

    private fun getJwt(): String? {
        return getJwt.execute()
    }

    private fun extractLoginFromJwt(jwt: String): String? {
        // Извлекаем логин из поля "sub"
        return try {
            JWT(jwt).getClaim("sub").asString().toString()
        } catch (e: Exception) {
            null
        }
    }

    private fun isRequestFree(): Boolean {
        return requestDone.value == true
    }

    // Registration check

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
}