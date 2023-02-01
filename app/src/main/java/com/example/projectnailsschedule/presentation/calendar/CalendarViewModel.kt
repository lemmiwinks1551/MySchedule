package com.example.projectnailsschedule.presentation.calendar

import android.os.Bundle
import androidx.lifecycle.ViewModel
import com.example.projectnailsschedule.domain.models.DateParams
import com.example.projectnailsschedule.domain.usecase.calendarUC.LoadCalendarUseCase
import com.example.projectnailsschedule.domain.usecase.calendarUC.SelectDateUseCase
import com.example.projectnailsschedule.domain.usecase.calendarUC.SelectNextMonthUseCase
import com.example.projectnailsschedule.domain.usecase.calendarUC.SelectPrevMonthUseCase
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.YearMonth
import java.time.ZoneId
import java.util.*

class CalendarViewModel(
    private val loadCalendarUseCase: LoadCalendarUseCase,
    private val selectDateUseCase: SelectDateUseCase,
    private val selectNextMonthUseCase: SelectNextMonthUseCase,
    private val selectPrevMonthUseCase: SelectPrevMonthUseCase
) : ViewModel() {

    private val log = this::class.simpleName
    var currentMonth: LocalDate = LocalDate.now()

    //
    var day: String = ""
    var month: String = ""
    var year: String = ""
    var extraMonth: Long = 0

    fun getDayStatus(dateParams: DateParams): DateParams {
        // get day status from data
        loadCalendarUseCase.execute(dateParams)
        return dateParams
    }

    fun selectDate(): Bundle {
        // create bundle
        val bundle = Bundle()

        // crete dateParams obj with chosen date
        val dateParams = DateParams(
            _id = null,
            date = "$day.$month.$year",
            status = null
        )

        // put dateParams to the bundle
        bundle.putParcelable("dateParams", dateParams)
        return bundle
    }

    fun changeMonth(operator: Char) {
        // change current month
        if (operator == '+') {
            currentMonth = currentMonth.plusMonths(1)
            CalendarAdapter.month++ // ??
            extraMonth++
        } else {
            currentMonth = currentMonth.minusMonths(1)
            CalendarAdapter.month-- // ??
            extraMonth--
        }
    }

    fun getMonthYearName(): String {
        // return current year and month in format "Январь 2000"
        val date = Date.from(currentMonth.atStartOfDay(ZoneId.systemDefault())!!.toInstant())
        val month = SimpleDateFormat("LLLL", Locale.getDefault()).format(date)
        val year: String = currentMonth.year.toString()
        return "$month $year"
    }

    fun daysInMonthArray(date: LocalDate): ArrayList<String> {
        // get days in current month in ArrayList<String>
        val daysInMonthArray = ArrayList<String>()

        // Получаем месяц
        val yearMonth = YearMonth.from(date)

        // Получаем длину месяца
        val daysInMonth = yearMonth.lengthOfMonth()

        // Получаем первый день текущего месяца
        val firstOfMonth: LocalDate = currentMonth.withDayOfMonth(1) ?: LocalDate.now()

        // Получаем день недели первого дня месяца
        val dayOfWeek = firstOfMonth.dayOfWeek.value - 1

        // Заполняем массив для отображения в RecyclerView
        // Учитываем пустые дни (дни прошлого месяца
        // TODO: 12.07.2022 Добавить дни прошлого и будущего месяцев
        for (i in 1..42) {
            if (i <= dayOfWeek || i > daysInMonth + dayOfWeek) {
                daysInMonthArray.add("")
            } else {
                daysInMonthArray.add((i - dayOfWeek).toString())
            }
        }

        return daysInMonthArray
    }

}