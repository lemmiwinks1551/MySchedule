package com.example.projectnailsschedule.presentation.account

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.auth0.android.jwt.JWT
import com.example.projectnailsschedule.domain.models.User
import com.example.projectnailsschedule.domain.usecase.account.GetJwt
import com.example.projectnailsschedule.domain.usecase.account.LoginUseCase
import com.example.projectnailsschedule.domain.usecase.account.LogoutUseCase
import com.example.projectnailsschedule.domain.usecase.account.RegisterUser
import com.example.projectnailsschedule.domain.usecase.account.SendAccConfirmation
import com.example.projectnailsschedule.domain.usecase.account.SendPasswordResetConfirmation
import com.example.projectnailsschedule.domain.usecase.account.SetJwt
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    private var loginUseCase: LoginUseCase,
    private var logoutUseCase: LogoutUseCase,
    private var registerUser: RegisterUser,
    private var sendAccConfirmation: SendAccConfirmation,
    private var sendPasswordResetConfirmation: SendPasswordResetConfirmation,
    private var setJwt: SetJwt,
    private var getJwt: GetJwt
) : ViewModel() {

    var user = MutableLiveData<User?>(null)
    var requestDone = MutableLiveData(true)

    val requestStarted: () -> Unit = { requestDone.postValue(false) }
    val requestFinished: () -> Unit = { requestDone.postValue(true) }
    val setUsername: (login: String) -> Unit = { user.postValue(User(it)) }
    val clearUser: () -> Unit = { user.postValue(null) }

    init {
        // Пробуем получить JWT из SharedPreference и установить username
        getJwt()?.let { extractLoginFromJwt(it) }?.let { setUsername(it) }
    }

    suspend fun login(login: String, password: String): Boolean {
        requestStarted()

        val jwt = loginUseCase.execute(login, password)

        // Установить в объект user его username
        jwt?.let { extractLoginFromJwt(it) }?.let { setUsername(it) }

        requestFinished()

        // Установить JWT в SharedPreference
        return setJwt.execute(jwt)
    }

    suspend fun logout(): Boolean {
        requestStarted()

        logoutUseCase.execute()

        clearUser()

        requestFinished()

        // Set JWT = null
        return setJwt.execute(null)
    }

    suspend fun registerUser(): Boolean {
        return registerUser.execute()
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

    private fun extractLoginFromJwt(jwt: String): String {
        // Извлекаем логин из поля "sub"
        return JWT(jwt).getClaim("sub").asString().toString()
    }

    private fun checkLogin() {

    }
}