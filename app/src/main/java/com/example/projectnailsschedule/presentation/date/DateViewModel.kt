package com.example.projectnailsschedule.presentation.date

import androidx.lifecycle.ViewModel
import com.example.projectnailsschedule.domain.usecase.dateUC.DeleteAppointmentUseCase
import com.example.projectnailsschedule.domain.usecase.dateUC.SetDateStatusUseCase

class DateViewModel(
    private var deleteAppointmentUseCase: DeleteAppointmentUseCase,
    private var setDateStatusUseCase: SetDateStatusUseCase
) : ViewModel() {

}