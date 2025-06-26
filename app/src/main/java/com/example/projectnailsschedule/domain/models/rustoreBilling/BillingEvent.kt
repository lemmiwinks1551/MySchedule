package com.example.projectnailsschedule.domain.models.rustoreBilling

sealed class BillingEvent {
    data class ShowDialog(val dialogInfo: InfoDialogState): BillingEvent()
    data class ShowError(val error: Throwable): BillingEvent()
    object RefreshPurchases: BillingEvent()
}
