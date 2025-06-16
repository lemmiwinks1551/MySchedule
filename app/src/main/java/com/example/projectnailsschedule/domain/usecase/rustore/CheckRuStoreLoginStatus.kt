package com.example.projectnailsschedule.domain.usecase.rustore

import android.util.Log
import ru.rustore.sdk.billingclient.RuStoreBillingClient
import ru.rustore.sdk.billingclient.model.user.UserAuthorizationStatus
import ru.rustore.sdk.core.tasks.Task
import javax.inject.Inject


class CheckRuStoreLoginStatus @Inject constructor(
    private val billingClient: RuStoreBillingClient
) {
    fun execute(): Task<UserAuthorizationStatus> {
        val result = billingClient.userInfo.getAuthorizationStatus()
            .addOnSuccessListener { status ->
                status.authorized
            }
            .addOnFailureListener {
                // Обработка ошибки
                Log.e("CheckRuStoreLoginStatus", it.message.toString())
                UserAuthorizationStatus(false)
            }
        return result
    }
}