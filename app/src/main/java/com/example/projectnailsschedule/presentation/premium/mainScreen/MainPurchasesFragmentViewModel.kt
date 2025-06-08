package com.example.projectnailsschedule.presentation.premium.mainScreen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectnailsschedule.R
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
import ru.rustore.sdk.billingclient.model.purchase.PurchaseState
import com.example.projectnailsschedule.domain.models.rustoreBilling.BillingEvent
import com.example.projectnailsschedule.domain.models.rustoreBilling.BillingState
import com.example.projectnailsschedule.domain.models.rustoreBilling.InfoDialogState
import java.util.UUID
import javax.inject.Inject

/**
 * https://www.rustore.ru/help/sdk/payments/kotlin-java/9-0-1#getpurchaseinfo - про класс Purchase
 * */

@HiltViewModel
class MainPurchasesFragmentViewModel @Inject constructor(
    private val billingClient: RuStoreBillingClient // SDK-клиент RuStore Billing
) : ViewModel() {
    private val availableProductIds = listOf(
        // ID доступных для покупки подписок https://console.rustore.ru/apps/2063509631/subscriptions
        "premium_monthly"
    )

    // StateFlow для передачи состояния во фрагмент (список продуктов, загрузка и т.д.)
    private val _state = MutableStateFlow(BillingState())
    val state: StateFlow<BillingState> = _state.asStateFlow()

    // SharedFlow для одноразовых событий (ошибки, диалоги и т.д.)
    private val _event = MutableSharedFlow<BillingEvent>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val event = _event.asSharedFlow()

    init {
        // При создании ViewModel сразу подгружаем список продуктов и текущие покупки
        updateProductsAndPurchases()
    }

    // Обработка клика на карточку продукта – инициируем покупку
    fun onProductClick(product: Product) {
        purchaseProduct(product)
    }

    // Загружаем продукты и покупки. После – обновляем список на экране.
    fun updateProductsAndPurchases() {
        _state.update { it.copy(isLoading = true) } // отображаем загрузку во фрагменте

        viewModelScope.launch {// в скоупе ViewModel
            runCatching { // try catch по котлински
                withContext(Dispatchers.IO) {
                    // Загружаем список продуктов и активных покупок (await приостанавливает выполнение до завершения)
                    val products = billingClient.products.getProducts(availableProductIds).await()
                    val purchases = billingClient.purchases.getPurchases().await()

                    products to purchases
                }
            }.onSuccess { (products, purchases) ->
                proceedUnfinishedPurchases(purchases) // обрабатываем покупки, которые ещё не подтверждены
                updateProductList(products, purchases) // обновляем список продуктов на экране
            }.onFailure(::handleError) // если ошибка – показываем её
        }
    }

    // Подтверждаем покупки, которые были оплачены, но ещё не подтверждены
    private fun proceedUnfinishedPurchases(purchases: List<Purchase>) {
        // функция завершает незавершенные покупки в статусе
        purchases.forEach { purchase ->
            if (purchase.developerPayload?.isNotEmpty() == true) {
                Log.w(
                    "RuStoreBillingClient",
                    "DeveloperPayloadInfo: ${purchase.developerPayload}"
                )
            }

            viewModelScope.launch {
                runCatching {
                    withContext(Dispatchers.IO) {
                        val purchaseId = purchase.purchaseId ?: return@withContext
                        when (purchase.purchaseState) {
                            PurchaseState.PAID -> {
                                // Подтверждаем покупку в RuStore, если оплата прошла
                                billingClient.purchases.confirmPurchase(purchaseId).await()
                            }

                            else -> Unit // другие состояния пропускаем
                        }
                    }
                }.onFailure(::handleError)
            }
        }
    }

    // Оставляем на экране только те продукты, которые ещё не куплены
    private fun updateProductList(products: List<Product>, purchases: List<Purchase>) {
        val nonBoughtProducts = products.filter { product ->
            purchases.none { product.productId == it.productId }
        }

        _state.update { currentState ->
            currentState.copy(
                products = nonBoughtProducts,
                isLoading = false
            )
        }
    }

    // Обработка ошибок – выводим ошибку через событие и отключаем индикатор загрузки
    private fun handleError(throwable: Throwable) {
        // отловили ошибку - отправляем эвент и стейт во фрагмент
        _event.tryEmit(BillingEvent.ShowError(throwable))
        _state.update { it.copy(isLoading = false) }
    }

    // Инициация покупки через RuStore SDK
    private fun purchaseProduct(product: Product) {
        val developerPayload = UUID.randomUUID().toString() // полезная нагрузка, опционально

        billingClient.purchases.purchaseProduct(
            productId = product.productId,
            developerPayload = developerPayload
        )
            .addOnSuccessListener { paymentResult ->
                handlePaymentResult(paymentResult, developerPayload)
            }
            .addOnFailureListener {
                setErrorStateOnFailure(it)
            }
    }

    // Обработка результата оплаты – если всё прошло успешно, подтверждаем покупку
    private fun handlePaymentResult(paymentResult: PaymentResult, developerPayload: String) {
        when (paymentResult) {
            is PaymentResult.Success -> {
                confirmPurchase(
                    purchaseId = paymentResult.purchaseId,
                    developerPayload = developerPayload,
                )
            }

            else -> Unit
        }
    }

    // Подтверждаем покупку на стороне RuStore
    private fun confirmPurchase(purchaseId: String, developerPayload: String) {
        _state.update { currentState ->
            currentState.copy(
                isLoading = true,
                snackbarResId = R.string.billing_purchase_confirm_in_progress // текст о подтверждении
            )
        }

        billingClient.purchases.confirmPurchase(purchaseId, developerPayload)
            .addOnSuccessListener { proceedSuccessConfirmation(it) }
            .addOnFailureListener { setErrorStateOnFailure(it) }
    }

    // Покупка успешно подтверждена – уведомляем пользователя
    private fun proceedSuccessConfirmation(response: Unit) {
        _event.tryEmit(
            BillingEvent.ShowDialog(
                InfoDialogState(
                    titleRes = R.string.billing_product_confirmed,
                    message = response.toString(), // response – Unit, по сути ничего не содержит
                )
            )
        )
        _state.update { currentState ->
            currentState.copy(isLoading = false, snackbarResId = null)
        }
    }

    // Ошибка в процессе покупки/подтверждения – обновляем состояние и отправляем событие
    private fun setErrorStateOnFailure(error: Throwable) {
        _event.tryEmit(BillingEvent.ShowError(error))
        _state.value = _state.value.copy(isLoading = false)
    }
}