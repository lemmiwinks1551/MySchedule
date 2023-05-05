package com.example.projectnailsschedule.util

import android.annotation.SuppressLint
import android.app.Activity
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.temporal.WeekFields
import java.util.*

/**
 * Вспомогательный класс, выполняющий коенвертации даты
 * */

class Util {

    val LOG = this::class.simpleName
    val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")

    @SuppressLint("SimpleDateFormat")
    fun dateConverter(day: String): String {
        /** Получаем день формате d.M.yyyy и конвертируем в формат dd.MM.yyyy */
        val parser = SimpleDateFormat("d.M.yyyy")
        val formatter = SimpleDateFormat("dd.MM.yyyy")
        return formatter.format(parser.parse(day)!!).toString()
    }

    fun dateConverterNew(day: String): String {
        /** Получаем день формате d.M.yyyy и конвертируем в формат dd.MM.yyyy */
        val parser = SimpleDateFormat("yyyy-MM-dd")
        val formatter = SimpleDateFormat("dd.MM.yyyy")
        return formatter.format(parser.parse(day)!!).toString()
    }

    fun hideKeyboard(activity: Activity) {
        val imm: InputMethodManager =
            activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        //Find the currently focused view, so we can grab the correct window token from it.
        var view = activity.currentFocus
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = View(activity)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun getArrayFromMonth(selectedDate: LocalDate): ArrayList<String> {
        // get days in current month in ArrayList<String>
        val daysInMonthArray = ArrayList<String>()

        // Получаем месяц
        val yearMonth = YearMonth.from(selectedDate)

        // Получаем длину месяца
        val daysInMonth = yearMonth.lengthOfMonth()

        // Получаем первый день текущего месяца
        val firstOfMonth: LocalDate = selectedDate.withDayOfMonth(1) ?: LocalDate.now()

        // Получаем день недели первого дня месяца
        val dayOfWeek = firstOfMonth.dayOfWeek.value - 1

        val maxGridValue = 7 * getWeeksInMonth(date = selectedDate)
        // Заполняем массив для отображения в RecyclerView
        // Учитываем пустые дни (дни прошлого месяца

        for (i in 1..maxGridValue) {
            if (i <= dayOfWeek || i > daysInMonth + dayOfWeek) {
                daysInMonthArray.add("")
            } else {
                daysInMonthArray.add((i - dayOfWeek).toString())
            }
        }
        return daysInMonthArray
    }

    private fun getWeeksInMonth(date: LocalDate): Int {
        // get weeks in month
        val locale = Locale("ru")
        val weekOfMonthStart = date.withDayOfMonth(1).get(WeekFields.of(locale).weekOfYear())
        val weekOfMonthEnd = date.withDayOfMonth(date.lengthOfMonth()).get(WeekFields.of(locale).weekOfYear())
        return weekOfMonthEnd - weekOfMonthStart + 1
    }
}