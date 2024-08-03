package com.example.projectnailsschedule.presentation.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.projectnailsschedule.domain.models.UserData
import com.example.projectnailsschedule.domain.models.UserDataManager
import com.example.projectnailsschedule.domain.usecase.apiUC.SendUserDataUseCase
import com.example.projectnailsschedule.domain.usecase.settingsUC.GetLanguageUseCase
import com.example.projectnailsschedule.domain.usecase.settingsUC.GetUserThemeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.LinkedList
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getLanguageUseCase: GetLanguageUseCase,
    private val getUserThemeUseCase: GetUserThemeUseCase,
    private val sendUserDataUseCase: SendUserDataUseCase
) : ViewModel() {

    var userDateQueue = MutableLiveData(LinkedList<UserData>())

    fun addUserData(event: String) {
        UserDataManager.updateUserData(event = event) // обновляем синглтон
        val queue = userDateQueue.value ?: LinkedList()
        queue.add(UserDataManager.getUserData()) // кладем обновленный синглтон в очередь
        userDateQueue.postValue(queue) // Обновление LiveData
    }

    suspend fun sendUserData() {
        val nexUserData = userDateQueue.value?.get(0)
        if (nexUserData != null) {
            sendUserDataUseCase.execute(nexUserData)
            userDateQueue.value?.removeAt(0)
            userDateQueue.postValue(userDateQueue.value) // Обновление LiveData
        }
    }

    fun getLanguage(): String {
        return getLanguageUseCase.execute()
    }

    fun getUserTheme(): String {
        return getUserThemeUseCase.execute()
    }
}




