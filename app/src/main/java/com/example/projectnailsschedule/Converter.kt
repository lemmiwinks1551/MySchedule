package com.example.projectnailsschedule

import android.annotation.SuppressLint
import android.util.Log
import java.text.SimpleDateFormat

class Converter {

    /** Вспомогательный класс, выполняющий различные конвертации */

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
}