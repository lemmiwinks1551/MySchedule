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

    private fun checkLogin() {

    }

    private fun isRequestFree(): Boolean {
        return requestDone.value == true
    }
}