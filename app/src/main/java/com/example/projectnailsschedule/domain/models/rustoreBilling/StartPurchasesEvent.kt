package com.example.projectnailsschedule.domain.models.rustoreBilling

import ru.rustore.sdk.billingclient.model.purchase.PurchaseAvailabilityResult

sealed class StartPurchasesEvent {
    // класс событий event

    data class PurchasesAvailability(
        val availability: PurchaseAvailabilityResult,
    ) : StartPurchasesEvent()

    data class Error(
        val throwable: Throwable,
    ) : StartPurchasesEvent()
}
