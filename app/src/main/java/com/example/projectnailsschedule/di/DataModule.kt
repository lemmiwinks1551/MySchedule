package com.example.projectnailsschedule.di

import android.content.Context
import com.example.projectnailsschedule.data.repository.ProcedureRepositoryImpl
import com.example.projectnailsschedule.data.repository.ScheduleRepositoryImpl
import com.example.projectnailsschedule.domain.repository.ProcedureRepository
import com.example.projectnailsschedule.domain.repository.ScheduleRepository
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
}