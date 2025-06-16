package com.example.projectnailsschedule.domain.models.rustoreBilling

import ru.rustore.sdk.core.feature.model.FeatureAvailabilityResult

data class StartPurchasesState(
    val isLoading: Boolean = false,
    val purchasesAvailability: FeatureAvailabilityResult? = null,
    val error: Throwable? = null,
    val isRuStoreLoggedIn: Boolean? = null,
    val isAccountLoggedIn: Boolean? = null
)
