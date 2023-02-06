package com.example.projectnailsschedule.util

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.example.projectnailsschedule.R
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.ArrayList

/**
 * Вспомогательный класс, выполняющий коенвертации даты
 * */

class Util {

    val LOG = this::class.simpleName

    fun addZero(digit: String): String {
        /** Дописывает 0 к однозначным числам */
        val output: String
        if (digit.length == 1) {
            output = "0$digit"
            Log.e(LOG, "Добавлен 0, возвращаем $output")
            return output
        } else {
            Log.e(LOG, "Добавлять 0 не требуется, возвращаем $digit")
            return digit
        }
    }

    fun removeZero(digit: String): String {
        /** Удаляем 0 у однозначных чисел */
        val output: String
        if (digit[0].toString() == "0") {
            output = digit.replace("0", "")
            Log.e(LOG, "Удален 0, возвращаем $output")
            return output
        } else {
            Log.e(LOG, "Удалять 0 не требуется, возвращаем $digit")
            return digit
        }
    }

    @SuppressLint("SimpleDateFormat")
    fun dateConverter(day: String): String {
        /** Получаем день формате d.M.yyyy и конвертируем в формат dd.MM.yyyy */
        val parser = SimpleDateFormat("d.M.yyyy")
        val formatter = SimpleDateFormat("dd.MM.yyyy")
        return formatter.format(parser.parse(day)).toString()
    }

    fun stringToLocalDate(date: String): LocalDate {
        /** Parse String "dd.MM.yyyy" into LocalDate */
        try {
            return LocalDate.parse(date, DateTimeFormatter.ofPattern("dd.MM.yyyy"))
        } catch (e: Exception) {
            Log.e(LOG, e.toString())
            return LocalDate.now()
        }
    }

    fun getWeekDayName(date: LocalDate, context: Context): String {
        /** Get name of week day for toolbar */
        with(context) {
            return when (date.dayOfWeek.value) {
                1 -> getString(R.string.mon)
                2 -> getString(R.string.tue)
                3 -> getString(R.string.wed)
                4 -> getString(R.string.thu)
                5 -> getString(R.string.fri)
                6 -> getString(R.string.sat)
                7 -> getString(R.string.sun)
                else -> {
                    Log.e(LOG, "No such string with that day of week")
                    ""
                }
            }
        }

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