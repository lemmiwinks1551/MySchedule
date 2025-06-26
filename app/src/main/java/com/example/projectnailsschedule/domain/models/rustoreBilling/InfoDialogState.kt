package com.example.projectnailsschedule.domain.models.rustoreBilling

import androidx.annotation.StringRes

data class InfoDialogState(
    @StringRes val titleRes: Int,
    val message: String
)
