package com.example.projectnailsschedule.presentation.about

import androidx.lifecycle.ViewModel
import com.example.projectnailsschedule.domain.models.UserDataManager
import com.example.projectnailsschedule.domain.usecase.util.UpdateUserDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AboutViewModel @Inject constructor(
    private var updateUserDataUseCase: UpdateUserDataUseCase
) : ViewModel() {

    fun updateUserData(event: String) {
        updateUserDataUseCase.execute(event)
    }
}
