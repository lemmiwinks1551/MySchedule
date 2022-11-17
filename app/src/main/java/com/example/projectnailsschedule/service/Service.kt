package com.example.projectnailsschedule.service

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.example.projectnailsschedule.R
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * Вспомогательный класс, выполняющий коенвертации даты
 * */

class Service {

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
        return LocalDate.parse(date, DateTimeFormatter.ofPattern("dd.MM.yyyy"))
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
}