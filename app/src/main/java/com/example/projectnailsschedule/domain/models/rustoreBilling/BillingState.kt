package com.example.projectnailsschedule.domain.models.rustoreBilling

import androidx.annotation.StringRes
import ru.rustore.sdk.billingclient.model.product.Product
import ru.rustore.sdk.billingclient.model.purchase.Purchase

data class BillingState(
    val isLoading: Boolean = false,
    val products: List<Product> = emptyList(),
    val purchases: List<Purchase> = emptyList(),
    @StringRes val snackbarResId: Int? = null
) {
    val isEmpty: Boolean = products.isEmpty() && !isLoading
}
