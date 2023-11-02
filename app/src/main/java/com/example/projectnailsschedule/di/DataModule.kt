package com.example.projectnailsschedule.di

import android.content.Context
import com.example.projectnailsschedule.data.repository.ClientRepositoryImpl
import com.example.projectnailsschedule.data.repository.ProcedureRepositoryImpl
import com.example.projectnailsschedule.data.repository.ScheduleRepositoryImpl
import com.example.projectnailsschedule.data.repository.SettingsRepositoryImpl
import com.example.projectnailsschedule.domain.repository.ClientsRepository
import com.example.projectnailsschedule.domain.repository.ProcedureRepository
import com.example.projectnailsschedule.domain.repository.ScheduleRepository
import com.example.projectnailsschedule.domain.repository.SettingsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Provides
    @Singleton
    fun provideScheduleRepository(@ApplicationContext context: Context): ScheduleRepository {
        return ScheduleRepositoryImpl(context)
    }

    @Provides
    @Singleton
    fun provideProcedureRepository(@ApplicationContext context: Context): ProcedureRepository {
        return ProcedureRepositoryImpl(context)
    }

    @Provides
    @Singleton
    fun provideClientsRepository(@ApplicationContext context: Context): ClientsRepository {
        return ClientRepositoryImpl(context)
    }

    @Provides
    @Singleton
    fun provideSettingsRepository(@ApplicationContext context: Context): SettingsRepository {
        return SettingsRepositoryImpl(context)
    }
}