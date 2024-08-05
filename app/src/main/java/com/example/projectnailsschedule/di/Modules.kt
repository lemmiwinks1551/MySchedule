package com.example.projectnailsschedule.di

import android.content.Context
import com.example.projectnailsschedule.domain.usecase.apiUC.SendUserDataUseCase
import com.example.projectnailsschedule.util.UncaughtExceptionHandler
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class Modules {

    @Provides
    @Singleton
    fun provideContext(@ApplicationContext context: Context): Context {
        return context
    }

    @Provides
    @Singleton
    fun provideUncaughtExceptionHandler(
        sendUserDataUseCase: SendUserDataUseCase
    ): Thread.UncaughtExceptionHandler {
        return UncaughtExceptionHandler(sendUserDataUseCase)
    }
}