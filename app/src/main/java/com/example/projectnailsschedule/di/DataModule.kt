package com.example.projectnailsschedule.di

import android.content.Context
import com.example.projectnailsschedule.data.repository.ClientRepositoryImpl
import com.example.projectnailsschedule.data.repository.CalendarRepositoryImpl
import com.example.projectnailsschedule.data.repository.ProcedureRepositoryImpl
import com.example.projectnailsschedule.data.repository.ScheduleRepositoryImpl
import com.example.projectnailsschedule.data.repository.SettingsRepositoryImpl
import com.example.projectnailsschedule.domain.repository.repo.ClientsRepository
import com.example.projectnailsschedule.domain.repository.repo.CalendarRepository
import com.example.projectnailsschedule.domain.repository.repo.ProcedureRepository
import com.example.projectnailsschedule.domain.repository.repo.ScheduleRepository
import com.example.projectnailsschedule.domain.repository.repo.SettingsRepository
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

    @Provides
    @Singleton
    fun provideDatesRepository(@ApplicationContext context: Context): CalendarRepository {
        return CalendarRepositoryImpl(context)
    }
}