package com.example.projectnailsschedule.domain.usecase.rustore

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.rustore.sdk.billingclient.RuStoreBillingClient
import ru.rustore.sdk.billingclient.model.purchase.Purchase
import javax.inject.Inject

class GetPurchasesUseCase @Inject constructor(
    private val billingClient: RuStoreBillingClient
) {
    suspend fun execute(): Result<List<Purchase>> {
        return runCatching {
            withContext(Dispatchers.IO) {
                billingClient.purchases.getPurchases().await()
            }
        }
    }
}