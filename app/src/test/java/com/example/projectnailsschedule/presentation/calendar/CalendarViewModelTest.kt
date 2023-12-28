package com.example.projectnailsschedule.presentation.calendar

import androidx.arch.core.executor.ArchTaskExecutor
import androidx.arch.core.executor.TaskExecutor
import com.example.projectnailsschedule.domain.models.AppointmentModelDb
import com.example.projectnailsschedule.domain.models.DateParams
import com.example.projectnailsschedule.domain.usecase.calendarUC.LoadShortDateUseCase
import com.example.projectnailsschedule.domain.usecase.calendarUC.SetSelectedMonthUc
import com.example.projectnailsschedule.domain.usecase.dateUC.GetDateAppointmentsUseCase
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.mock

internal class CalendarViewModelTest {

    lateinit var calendarViewModel: CalendarViewModel

    private val loadShortDateUseCase = mock<LoadShortDateUseCase>()
    private var getDateAppointmentsUseCase = mock<GetDateAppointmentsUseCase>()
    private var setSelectedMonthUc = mock<SetSelectedMonthUc>()

    @AfterEach
    fun afterEach() {
        Mockito.reset(loadShortDateUseCase)
        Mockito.reset(getDateAppointmentsUseCase)
        Mockito.reset(setSelectedMonthUc)
        ArchTaskExecutor.getInstance().setDelegate(null)
    }

    @BeforeEach
    fun beforeEach() {

        calendarViewModel = CalendarViewModel(
            loadShortDateUseCase,
            getDateAppointmentsUseCase,
            setSelectedMonthUc
        )
        ArchTaskExecutor.getInstance().setDelegate(object : TaskExecutor() {
            override fun executeOnDiskIO(runnable: Runnable) {
                runnable.run()
            }

            override fun postToMainThread(runnable: Runnable) {
                runnable.run()
            }

            override fun isMainThread(): Boolean {
                return true
            }

        })
    }

    @Test
    fun `should return array of appointments by DateParams`() {
        val testDateParams = DateParams()
        val testAppointmentModelDb = AppointmentModelDb(deleted = false)
        val arrayOfAppointments = arrayOf(testAppointmentModelDb)

        Mockito.`when`(calendarViewModel.getArrayAppointments(testDateParams))
            .thenReturn(arrayOfAppointments)

        val actual = calendarViewModel.getArrayAppointments(testDateParams)
        val expected = arrayOfAppointments

        Assertions.assertEquals(expected, actual)
    }

}