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
import com.example.projectnailsschedule.domain.usecase.calendarUC.GetDateAppointments
import com.example.projectnailsschedule.domain.usecase.calendarUC.InsertCalendarDateUseCase
import com.example.projectnailsschedule.domain.usecase.clientsUC.DeleteClientUseCase
import com.example.projectnailsschedule.domain.usecase.clientsUC.InsertClientUseCase
import com.example.projectnailsschedule.domain.usecase.clientsUC.SearchClientUseCase
import com.example.projectnailsschedule.domain.usecase.clientsUC.UpdateClientUseCase
import com.example.projectnailsschedule.domain.usecase.appointmentUC.DeleteAppointmentUseCase
import com.example.projectnailsschedule.domain.usecase.importExportUC.ExportUseCase
import com.example.projectnailsschedule.domain.usecase.importExportUC.ImportUseCase
import com.example.projectnailsschedule.domain.usecase.util.RestartAppUseCase
import com.example.projectnailsschedule.domain.usecase.proceduresUC.DeleteProcedureUseCase
import com.example.projectnailsschedule.domain.usecase.proceduresUC.InsertProcedureUseCase
import com.example.projectnailsschedule.domain.usecase.proceduresUC.SearchProcedureUseCase
import com.example.projectnailsschedule.domain.usecase.proceduresUC.UpdateProcedureUseCase
import com.example.projectnailsschedule.domain.usecase.appointmentUC.SearchAppointmentUseCase
import com.example.projectnailsschedule.domain.usecase.clientsUC.GetClientByIdUseCase
import com.example.projectnailsschedule.domain.usecase.settingsUC.*
import com.example.projectnailsschedule.domain.usecase.socUC.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class DomainModule {

    // Appointments

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
    fun gateDateAppointmentsUseCase(repository: ScheduleRepository): GetDateAppointments {
        return GetDateAppointments(repository)
    }

    // Clients

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
    fun provideSearchClientUseCase(repository: ClientsRepository): SearchClientUseCase {
        return SearchClientUseCase(repository)
    }

    @Provides
    fun getClientByIdUseCase(repository: ClientsRepository): GetClientByIdUseCase {
        return GetClientByIdUseCase(repository)
    }

    // Procedures

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

    // Soc

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

    // Import Export

    @Provides
    fun provideImportUseCase(context: Context): ImportUseCase {
        return ImportUseCase(context)
    }

    @Provides
    fun provideExportUseCase(context: Context): ExportUseCase {
        return ExportUseCase(context)
    }

    // Date colors

    @Provides
    fun provideGetDateColorUseCase(repository: CalendarRepository): SelectCalendarDateByDateUseCase {
        return SelectCalendarDateByDateUseCase(repository)
    }

    @Provides
    fun provideSetDateColorUseCase(repository: CalendarRepository): InsertCalendarDateUseCase {
        return InsertCalendarDateUseCase(repository)
    }

    @Provides
    fun provideDeleteCalendarObj(repository: CalendarRepository): CalendarDbDeleteObj {
        return CalendarDbDeleteObj(repository)
    }

    // Settings

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

    // Util

    @Provides
    fun provideRestartAppUseCase(context: Context): RestartAppUseCase {
        return RestartAppUseCase(context)
    }
}