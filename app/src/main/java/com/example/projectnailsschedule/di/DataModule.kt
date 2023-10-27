package com.example.projectnailsschedule.di

import android.content.Context
import com.example.projectnailsschedule.data.repository.ScheduleRepositoryImpl
import com.example.projectnailsschedule.domain.repository.ScheduleRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
class DataModule {

    @Provides
    fun provideScheduleRepository(@ApplicationContext context: Context): ScheduleRepository {
        return ScheduleRepositoryImpl(context)
    }
}