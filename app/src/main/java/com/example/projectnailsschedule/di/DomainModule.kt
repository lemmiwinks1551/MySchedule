package com.example.projectnailsschedule.di

import android.content.Context
import com.example.projectnailsschedule.domain.repository.repo.CalendarRepository
import com.example.projectnailsschedule.domain.repository.repo.ClientsRepository
import com.example.projectnailsschedule.domain.repository.repo.ProcedureRepository
import com.example.projectnailsschedule.domain.repository.repo.ScheduleRepository
import com.example.projectnailsschedule.domain.repository.repo.SettingsRepository
import com.example.projectnailsschedule.domain.usecase.account.GetJwt
import com.example.projectnailsschedule.domain.usecase.account.LoginUseCase
import com.example.projectnailsschedule.domain.usecase.account.LogoutUseCase
import com.example.projectnailsschedule.domain.usecase.account.RegistrationUseCase
import com.example.projectnailsschedule.domain.usecase.account.SendAccConfirmation
import com.example.projectnailsschedule.domain.usecase.account.SendPasswordResetConfirmation
import com.example.projectnailsschedule.domain.usecase.account.SetJwt
import com.example.projectnailsschedule.domain.usecase.apiUC.GetFaqUseCase
import com.example.projectnailsschedule.domain.usecase.apiUC.GetProductionCalendarDateInfoUseCase
import com.example.projectnailsschedule.domain.usecase.apiUC.GetProductionCalendarYearUseCase
import com.example.projectnailsschedule.domain.usecase.apiUC.SendUserDataUseCase
import com.example.projectnailsschedule.domain.usecase.appointmentUC.DeleteAppointmentUseCase
import com.example.projectnailsschedule.domain.usecase.appointmentUC.InsertAppointmentUseCase
import com.example.projectnailsschedule.domain.usecase.appointmentUC.SearchAppointmentUseCase
import com.example.projectnailsschedule.domain.usecase.appointmentUC.UpdateAppointmentUseCase
import com.example.projectnailsschedule.domain.usecase.appointmentUC.UpdateClientInAppointmentsUseCase
import com.example.projectnailsschedule.domain.usecase.calendarUC.CalendarDbDeleteObj
import com.example.projectnailsschedule.domain.usecase.calendarUC.GetDateAppointments
import com.example.projectnailsschedule.domain.usecase.calendarUC.InsertCalendarDateUseCase
import com.example.projectnailsschedule.domain.usecase.calendarUC.SelectCalendarDateByDateUseCase
import com.example.projectnailsschedule.domain.usecase.clientsUC.DeleteClientUseCase
import com.example.projectnailsschedule.domain.usecase.clientsUC.GetClientByIdUseCase
import com.example.projectnailsschedule.domain.usecase.clientsUC.InsertClientUseCase
import com.example.projectnailsschedule.domain.usecase.clientsUC.SearchClientUseCase
import com.example.projectnailsschedule.domain.usecase.clientsUC.UpdateClientUseCase
import com.example.projectnailsschedule.domain.usecase.importExportUC.ExportUseCase
import com.example.projectnailsschedule.domain.usecase.importExportUC.ImportUseCase
import com.example.projectnailsschedule.domain.usecase.proceduresUC.DeleteProcedureUseCase
import com.example.projectnailsschedule.domain.usecase.proceduresUC.InsertProcedureUseCase
import com.example.projectnailsschedule.domain.usecase.proceduresUC.SearchProcedureUseCase
import com.example.projectnailsschedule.domain.usecase.proceduresUC.UpdateProcedureUseCase
import com.example.projectnailsschedule.domain.usecase.settingsUC.*
import com.example.projectnailsschedule.domain.usecase.socUC.*
import com.example.projectnailsschedule.domain.usecase.util.RestartAppUseCase
import com.example.projectnailsschedule.domain.usecase.util.UpdateUserDataUseCase
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

    @Provides
    fun updateClientInAppointmentsUseCase(repository: ScheduleRepository): UpdateClientInAppointmentsUseCase {
        return UpdateClientInAppointmentsUseCase(repository)
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

    @Provides
    fun provideUpdateUserDataUseCase(): UpdateUserDataUseCase {
        return UpdateUserDataUseCase()
    }

    // Server API

    @Provides
    fun providePostUserDataUseCase(context: Context): SendUserDataUseCase {
        return SendUserDataUseCase(context)
    }

    @Provides
    fun getProductionCalendarUseCase(context: Context): GetProductionCalendarDateInfoUseCase {
        return GetProductionCalendarDateInfoUseCase(context)
    }

    @Provides
    fun getProductionCalendarYearUseCase(context: Context): GetProductionCalendarYearUseCase {
        return GetProductionCalendarYearUseCase(context)
    }

    @Provides
    fun getFaqUseCase(context: Context): GetFaqUseCase {
        return GetFaqUseCase(context)
    }

    // Account API

    @Provides
    fun provideLoginUseCase(context: Context): LoginUseCase {
        return LoginUseCase(context)
    }

    @Provides
    fun provideLogoutUseCase(context: Context): LogoutUseCase {
        return LogoutUseCase(context)
    }

    @Provides
    fun provideRegisterNewUserUseCase(context: Context): RegistrationUseCase {
        return RegistrationUseCase(context)
    }

    @Provides
    fun provideSendAccountConfirmation(context: Context): SendAccConfirmation {
        return SendAccConfirmation(context)
    }

    @Provides
    fun provideSendPasswordResetConfirmation(context: Context): SendPasswordResetConfirmation {
        return SendPasswordResetConfirmation(context)
    }

    @Provides
    fun setJwt(settingsRepository: SettingsRepository): SetJwt {
        return SetJwt(settingsRepository)
    }

    @Provides
    fun getJwt(settingsRepository: SettingsRepository): GetJwt {
        return GetJwt(settingsRepository)
    }
}