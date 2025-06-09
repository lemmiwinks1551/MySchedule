package com.example.projectnailsschedule.presentation.premium.startScreen

import androidx.lifecycle.ViewModel
import com.example.projectnailsschedule.domain.models.dto.UserInfoDtoManager
import com.example.projectnailsschedule.domain.models.rustoreBilling.StartPurchasesEvent
import com.example.projectnailsschedule.domain.models.rustoreBilling.StartPurchasesState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import ru.rustore.sdk.billingclient.RuStoreBillingClient
import ru.rustore.sdk.billingclient.utils.pub.checkPurchasesAvailability
import javax.inject.Inject

/** ViewModel для StartPurchasesFragment, обратывает клик по кнопке и проверят доступность покупок
 * Версия как в документации к SDK
 * Устарел, т.к. покупки больше не требуют установленного RuStore и авторизации в нем */

@HiltViewModel
class StartPurchasesViewModel @Inject constructor() : ViewModel() {

    // поток state
    private val _state = MutableStateFlow(StartPurchasesState())
    val state = _state.asStateFlow()

    // поток event
    private val _event = MutableSharedFlow<StartPurchasesEvent>(
        extraBufferCapacity = 1, // удерживает одно событие в буфере, даже если никто не слушает
        onBufferOverflow = BufferOverflow.DROP_OLDEST // удаляем последнее событие и отдаем новое, если буфер переполнен
    )
    val event = _event.asSharedFlow()

    fun checkPurchasesAvailability() {
        // пользователь кликает по кнопке "Начать покупки"
        _state.update { it.copy(isLoading = true) } // показываем, что загрузка началась

        if (!requireUserLoginOrEmitError()) return

        RuStoreBillingClient.checkPurchasesAvailability()
            // по факту не имеет смысла, т.к. работает и без авторизации в RuStore и даже с удаленным RuStore
            .addOnSuccessListener { result ->
                // Если покупки доступны
                _state.update { it.copy(isLoading = false) } // показываем, что загрузка завершена
                _event.tryEmit(StartPurchasesEvent.PurchasesAvailability(result)) // эмитим в event статус
            }
            .addOnFailureListener { throwable ->
                // Если покупки не доступны
                _state.update { it.copy(isLoading = false) }
                _event.tryEmit(StartPurchasesEvent.Error(throwable))
            }
    }

    private fun requireUserLoginOrEmitError(): Boolean {
        // если пользователь не зашел в аакаунт - не даем ниче покупать, т.к. нужен аккаунт и логин для работы с сервером
        return if (UserInfoDtoManager.getUserDto() == null) {
            _event.tryEmit(StartPurchasesEvent.Error(Throwable("Необходимо зарегистрироваться и войти в аккаунт")))
            _state.update { it.copy(isLoading = false) }

            false
        } else true
    }
}