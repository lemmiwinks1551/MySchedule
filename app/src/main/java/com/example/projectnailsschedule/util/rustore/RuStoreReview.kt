package com.example.projectnailsschedule.util.rustore

import android.content.Context
import android.util.Log
import com.example.projectnailsschedule.data.repository.ScheduleRepositoryImpl
import com.example.projectnailsschedule.domain.repository.ScheduleRepository
import com.example.projectnailsschedule.domain.usecase.appointmentUC.GetAllAppointmentsUseCase
import com.example.projectnailsschedule.domain.usecase.dateUC.GetDateAppointmentsUseCase
import ru.rustore.sdk.core.tasks.OnCompleteListener
import ru.vk.store.sdk.review.RuStoreReviewManagerFactory
import ru.vk.store.sdk.review.model.ReviewInfo

/** Class for asking user`s review */

class RuStoreReview(val context: Context) {

    private val log = "RuStoreReview"
    private val minAppointments = 10

    private fun getAllAppointmentsCount(): Int {
        val scheduleRepositoryImpl = ScheduleRepositoryImpl(context)
        return GetAllAppointmentsUseCase(scheduleRepositoryImpl).execute().size
    }

    fun rateApp() {
        val appCount = getAllAppointmentsCount()
        if (appCount > minAppointments) {
            Log.e(log, "Appointments count > 10. Ask for review")

            // Запустить оценку приложения, только если пользователь создал 10 записей в нем
            // Для работы с оценками необходимо создать RuStoreReviewManager с помощью RuStoreReviewManagerFactory:
            val manager = RuStoreReviewManagerFactory.create(context)

            // Вызовите requestReviewFlow() заранее перед вызовом launchReviewFlow(reviewInfo),
            // чтобы подготовить необходимую информацию для отображения экрана.
            // ReviewInfo имеет свой срок жизни — около пяти минут.
            manager.requestReviewFlow().addOnCompleteListener(object : OnCompleteListener<ReviewInfo> {
                override fun onFailure(throwable: Throwable) {
                    // Handle error
                    // Если получен ответ onFailure, то не рекомендуем самостоятельно отображать ошибку пользователю,
                    // так как пользователь не запускал данный процесс.
                    Log.e(log, throwable.message.toString())
                }

                override fun onSuccess(result: ReviewInfo) {
                    // Save reviewInfo
                    // Если получен ответ onSuccess, то необходимо локально сохранить ReviewInfo,
                    // для последующего вызова launchReviewFlow(reviewInfo).
                    Log.e(log, "onSuccess")
                    manager.launchReviewFlow(result) // Запуска формы запроса оценки и отзыва о приложении у пользователя
                }
            })
        } else {
            Log.e(log, "Appointments count < 10. Don`t ask for review")
        }
    }
}