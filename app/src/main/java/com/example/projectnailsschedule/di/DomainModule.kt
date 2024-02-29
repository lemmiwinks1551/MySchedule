package com.example.projectnailsschedule.di

import android.content.Context
import com.example.projectnailsschedule.domain.repository.ClientsRepository
import com.example.projectnailsschedule.domain.repository.CalendarRepository
import com.example.projectnailsschedule.domain.repository.ProcedureRepository
import com.example.projectnailsschedule.domain.repository.ScheduleRepository
import com.example.projectnailsschedule.domain.repository.SettingsRepository
import com.example.projectnailsschedule.domain.usecase.appointmentUC.InsertAppointmentUseCase
import com.example.projectnailsschedule.domain.usecase.appointmentUC.UpdateAppointmentUseCase
import com.example.projectnailsschedule.domain.usecase.calendarUC.CalendarDbDeleteObj
import com.example.projectnailsschedule.domain.usecase.calendarUC.SelectCalendarDateByDateUseCase
import com.example.projectnailsschedule.domain.usecase.calendarUC.LoadShortDateUseCase
import com.example.projectnailsschedule.domain.usecase.calendarUC.InsertCalendarDateUseCase
import com.example.projectnailsschedule.domain.usecase.calendarUC.UpdateDateColorUseCase
import com.example.projectnailsschedule.domain.usecase.clientsUC.DeleteClientUseCase
import com.example.projectnailsschedule.domain.usecase.clientsUC.InsertClientUseCase
import com.example.projectnailsschedule.domain.usecase.clientsUC.SearchClientUseCase
import com.example.projectnailsschedule.domain.usecase.clientsUC.UpdateClientUseCase
import com.example.projectnailsschedule.domain.usecase.dateUC.DeleteAppointmentUseCase
import com.example.projectnailsschedule.domain.usecase.dateUC.GetDateAppointmentsUseCase
import com.example.projectnailsschedule.domain.usecase.importExportUC.ExportUseCase
import com.example.projectnailsschedule.domain.usecase.importExportUC.ImportUseCase
import com.example.projectnailsschedule.domain.usecase.importExportUC.RestartAppUseCase
import com.example.projectnailsschedule.domain.usecase.proceduresUC.DeleteProcedureUseCase
import com.example.projectnailsschedule.domain.usecase.proceduresUC.InsertProcedureUseCase
import com.example.projectnailsschedule.domain.usecase.proceduresUC.SearchProcedureUseCase
import com.example.projectnailsschedule.domain.usecase.proceduresUC.UpdateProcedureUseCase
import com.example.projectnailsschedule.domain.usecase.searchUC.GetAllAppointmentsLiveDataUseCase
import com.example.projectnailsschedule.domain.usecase.searchUC.SearchAppointmentUseCase
import com.example.projectnailsschedule.domain.usecase.settingsUC.*
import com.example.projectnailsschedule.domain.usecase.socUC.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class DomainModule {

    @Provides
    fun provideInsertAppointmentUseCase(repository: ScheduleRepository): InsertAppointmentUseCase {
        return InsertAppointmentUseCase(repository)
    }

    @Provides
    fun provideUpdateAppointmentUseCase(repository: ScheduleRepository): UpdateAppointmentUseCase {
        return UpdateAppointmentUseCase(repository)
    }

    @Provides
    fun provideDeleteAppointmentUseCase(repository: ScheduleRepository): DeleteAppointmentUseCase {
        return DeleteAppointmentUseCase(repository)
    }

    @Provides
    fun provideSearchAppointmentUseCase(repository: ScheduleRepository): SearchAppointmentUseCase {
        return SearchAppointmentUseCase(repository)
    }

    @Provides
    fun provideGetAllAppointmentsLiveDataUseCase(repository: ScheduleRepository): GetAllAppointmentsLiveDataUseCase {
        return GetAllAppointmentsLiveDataUseCase(repository)
    }

    @Provides
    fun provideInsertClientUseCase(repository: ClientsRepository): InsertClientUseCase {
        return InsertClientUseCase(repository)
    }

    @Provides
    fun provideUpdateClientUseCase(repository: ClientsRepository): UpdateClientUseCase {
        return UpdateClientUseCase(repository)
    }

    @Provides
    fun provideDeleteClientUseCase(repository: ClientsRepository): DeleteClientUseCase {
        return DeleteClientUseCase(repository)
    }

    @Provides
    fun provideInsertProcedureUseCase(repository: ProcedureRepository): InsertProcedureUseCase {
        return InsertProcedureUseCase(repository)
    }

    @Provides
    fun provideUpdateProcedureUseCase(repository: ProcedureRepository): UpdateProcedureUseCase {
        return UpdateProcedureUseCase(repository)
    }

    @Provides
    fun provideDeleteProcedureUseCase(repository: ProcedureRepository): DeleteProcedureUseCase {
        return DeleteProcedureUseCase(repository)
    }

    @Provides
    fun provideSearchProcedureUseCase(repository: ProcedureRepository): SearchProcedureUseCase {
        return SearchProcedureUseCase(repository)
    }

    @Provides
    fun provideSearchClientUseCase(repository: ClientsRepository): SearchClientUseCase {
        return SearchClientUseCase(repository)
    }

    @Provides
    fun provideLoadShortDateUseCase(repository: ScheduleRepository): LoadShortDateUseCase {
        return LoadShortDateUseCase(repository)
    }

    @Provides
    fun provideGetDateAppointmentsUseCase(repository: ScheduleRepository): GetDateAppointmentsUseCase {
        return GetDateAppointmentsUseCase(repository)
    }

    @Provides
    fun provideStartVkUc(context: Context): StartVkUc {
        return StartVkUc(context)
    }

    @Provides
    fun provideStartTelegramUc(context: Context): StartTelegramUc {
        return StartTelegramUc(context)
    }

    @Provides
    fun provideStartInstagramUc(context: Context): StartInstagramUc {
        return StartInstagramUc(context)
    }

    @Provides
    fun provideStartWhatsAppUc(context: Context): StartWhatsAppUc {
        return StartWhatsAppUc(context)
    }

    @Provides
    fun provideStartPhoneUc(context: Context): StartPhoneUc {
        return StartPhoneUc(context)
    }

    @Provides
    fun provideRestartAppUseCase(context: Context): RestartAppUseCase {
        return RestartAppUseCase(context)
    }

    @Provides
    fun provideImportUseCase(context: Context): ImportUseCase {
        return ImportUseCase(context)
    }

    @Provides
    fun provideExportIseCase(context: Context): ExportUseCase {
        return ExportUseCase(context)
    }

    @Provides
    fun provideGetLanguageUseCase(repository: SettingsRepository): GetLanguageUseCase {
        return GetLanguageUseCase(repository)
    }

    @Provides
    fun provideSetLightThemeUseCase(repository: SettingsRepository): SetLightThemeUseCase {
        return SetLightThemeUseCase(repository)
    }

    @Provides
    fun provideGetThemeUseCase(repository: SettingsRepository): GetThemeUseCase {
        return GetThemeUseCase(repository)
    }

    @Provides
    fun provideSetDarkThemeUseCase(repository: SettingsRepository): SetDarkThemeUseCase {
        return SetDarkThemeUseCase(repository)
    }

    @Provides
    fun provideSetLanguageUseCase(repository: SettingsRepository): SetLanguageUseCase {
        return SetLanguageUseCase(repository)
    }

    @Provides
    fun provideSetUserThemeUseCase(repository: SettingsRepository): SetUserThemeUseCase {
        return SetUserThemeUseCase(repository)
    }

    @Provides
    fun provideGetUserThemeUseCase(repository: SettingsRepository): GetUserThemeUseCase {
        return GetUserThemeUseCase(repository)
    }

    @Provides
    fun provideGetDateColorUseCase(repository: CalendarRepository): SelectCalendarDateByDateUseCase {
        return SelectCalendarDateByDateUseCase(repository)
    }

    @Provides
    fun provideSetDateColorUseCase(repository: CalendarRepository): InsertCalendarDateUseCase {
        return InsertCalendarDateUseCase(repository)
    }

    @Provides
    fun provideUpdateDateColorUseCase(repository: CalendarRepository): UpdateDateColorUseCase {
        return UpdateDateColorUseCase(repository)
    }

    @Provides
    fun provideDeleteCalendarObj(repository: CalendarRepository): CalendarDbDeleteObj {
        return CalendarDbDeleteObj(repository)
    }
}