package com.example.projectnailsschedule.presentation.premium.mainScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectnailsschedule.R
import com.example.projectnailsschedule.domain.models.rustoreBilling.BillingEvent
import com.example.projectnailsschedule.domain.models.rustoreBilling.BillingState
import com.example.projectnailsschedule.domain.models.rustoreBilling.InfoDialogState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.rustore.sdk.billingclient.RuStoreBillingClient
import ru.rustore.sdk.billingclient.model.product.Product
import ru.rustore.sdk.billingclient.model.purchase.PaymentResult
import ru.rustore.sdk.billingclient.model.purchase.Purchase
import java.util.UUID
import javax.inject.Inject

/**
 * https://www.rustore.ru/help/sdk/payments/kotlin-java/9-0-1#getpurchaseinfo - про класс Purchase
 * */

@HiltViewModel
class MainPurchasesFragmentViewModel @Inject constructor(
    private val billingClient: RuStoreBillingClient
) : ViewModel() {

    private val availableProductIds = listOf("premium_monthly")

    private val _state = MutableStateFlow(BillingState())
    val state: StateFlow<BillingState> = _state.asStateFlow()

    private val _event = MutableSharedFlow<BillingEvent>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val event = _event.asSharedFlow()

    init {
        updateProductsAndPurchases()
    }

    fun onProductClick(product: Product) {
        purchaseProduct(product)
    }

    fun updateProductsAndPurchases() {
        _state.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            runCatching {
                withContext(Dispatchers.IO) {
                    val products = billingClient.products.getProducts(availableProductIds).await()
                    val purchases = billingClient.purchases.getPurchases().await()
                    products to purchases
                }
            }.onSuccess { (products, purchases) ->
                updateProductAndPurchasesList(products, purchases)
            }.onFailure(::handleError)
        }
    }

    private fun updateProductAndPurchasesList(products: List<Product>, purchases: List<Purchase>) {
        val nonBoughtProducts = products.filter { product ->
            purchases.none { product.productId == it.productId }
        }

        _state.update {
            it.copy(
                products = nonBoughtProducts,
                purchases = purchases,
                isLoading = false
            )
        }
    }

    private fun handleError(error: Throwable) {
        _event.tryEmit(BillingEvent.ShowError(error))
        _state.update { it.copy(isLoading = false) }
    }

    private fun purchaseProduct(product: Product) {
        val developerPayload = UUID.randomUUID().toString()

        billingClient.purchases.purchaseProduct(
            productId = product.productId,
            developerPayload = developerPayload
        )
            .addOnSuccessListener {
                handlePaymentResult(it)
            }
            .addOnFailureListener(::setErrorStateOnFailure)
    }

    private fun handlePaymentResult(paymentResult: PaymentResult) {
        if (paymentResult is PaymentResult.Success) {
            _event.tryEmit(
                BillingEvent.ShowDialog(
                    InfoDialogState(
                        titleRes = R.string.billing_product_confirmed,
                        message = "Перейдите в настройки, чтобы выключить синхронизацию"
                    )
                )
            )
            _event.tryEmit(BillingEvent.RefreshPurchases)
            _state.update { it.copy(isLoading = false, snackbarResId = null) }
        }
    }

    private fun setErrorStateOnFailure(error: Throwable) {
        _event.tryEmit(BillingEvent.ShowError(error))
        _state.update { it.copy(isLoading = false) }
    }
}
