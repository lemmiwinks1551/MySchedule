package com.example.projectnailsschedule.presentation.calendar

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.projectnailsschedule.domain.models.DateParams
import com.example.projectnailsschedule.domain.usecase.calendarUC.LoadCalendarUseCase
import com.example.projectnailsschedule.domain.usecase.calendarUC.SelectDateUseCase
import com.example.projectnailsschedule.domain.usecase.calendarUC.SelectNextMonthUseCase
import com.example.projectnailsschedule.domain.usecase.calendarUC.SelectPrevMonthUseCase
import com.example.projectnailsschedule.util.Service
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

    var selectedDate = MutableLiveData<LocalDate?>()

    init {
        selectedDate.value = LocalDate.now()
    }

    fun getDayStatus(dateParams: DateParams): DateParams {
        // get day status from repository
        loadCalendarUseCase.execute(dateParams)
        return dateParams
    }

    fun chooseDay(day: Int) {
        selectedDate.value = selectedDate.value?.withDayOfMonth(day)
        Log.e(log, "Chosen date ${selectedDate.value.toString()}")

    }

    fun goIntoDate(): Bundle {
        // create bundle
        val bundle = Bundle()

        // crete dateParams obj with chosen date
        val dateParams = DateParams(
            _id = null,
            date = selectedDate.value,
            status = null
        )

        // put dateParams to the bundle
        bundle.putParcelable("dateParams", dateParams)
        return bundle
    }

    fun changeMonth(operator: Char) {
        // change current month
        if (operator == '+') {
            selectedDate.value = selectedDate.value?.plusMonths(1)
            Log.e(log, "Month +")
        } else {
            selectedDate.value = selectedDate.value?.minusMonths(1)
            Log.e(log, "Month -")
        }
    }

    fun daysInMonthArray(): ArrayList<String> {
        // TODO: УБРАТЬ РЕТЕРН
        // get days in current month in ArrayList<String>
        val daysInMonthArray = ArrayList<String>()

        // Получаем месяц
        val yearMonth = YearMonth.from(selectedDate.value)

        // Получаем длину месяца
        val daysInMonth = yearMonth.lengthOfMonth()

        // Получаем первый день текущего месяца
        val firstOfMonth: LocalDate = selectedDate.value?.withDayOfMonth(1) ?: LocalDate.now()

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