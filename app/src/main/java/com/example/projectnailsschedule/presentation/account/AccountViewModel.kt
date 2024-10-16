package com.example.projectnailsschedule.presentation.account

import androidx.lifecycle.ViewModel
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

    var user: User? = null

    suspend fun login(login: String, password: String): Boolean {
        val jwt = loginUseCase.execute(login, password)
        // Set JWT
        return setJwt.execute(jwt)
    }

    suspend fun logout(): Boolean {
        logoutUseCase.execute()
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

    suspend fun getJwt(): String? {
        return getJwt.execute()
    }
}