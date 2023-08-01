package com.example.projectnailsschedule.util

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.example.projectnailsschedule.data.repository.ScheduleRepositoryImpl
import com.example.projectnailsschedule.data.storage.ScheduleDb
import com.example.projectnailsschedule.domain.models.AppointmentModelDb
import com.example.projectnailsschedule.domain.usecase.appointmentUC.SaveAppointmentUseCase
import com.example.projectnailsschedule.presentation.appointment.AppointmentViewModel
import com.example.projectnailsschedule.presentation.main.MainActivity
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.time.temporal.WeekFields
import java.util.*
import kotlin.collections.ArrayList

/**
 * Вспомогательный класс
 * */

class Util() {

    val log = this::class.simpleName
    val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")

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

    fun getArrayFromMonth2(selectedDate: LocalDate): ArrayList<String> {
        val arrayList = ArrayList<String>()
        for (i in 1..selectedDate.lengthOfMonth()) {
            arrayList.add(i.toString())
        }
        return arrayList
    }

    private fun getWeeksInMonth(date: LocalDate): Int {
        // get weeks in month
        val locale = Locale("ru")
        val weekOfMonthStart = date.withDayOfMonth(1).get(WeekFields.of(locale).weekOfYear())
        val weekOfMonthEnd =
            date.withDayOfMonth(date.lengthOfMonth()).get(WeekFields.of(locale).weekOfYear())
        return weekOfMonthEnd - weekOfMonthStart + 1
    }

    fun convertStringToLocalDate(dateString: String): LocalDate? {
        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
        return try {
            LocalDate.parse(dateString, formatter)
        } catch (e: DateTimeParseException) {
            null
        }
    }

    fun getDayOfWeek(date: String): String {
        val sdf = SimpleDateFormat("dd.MM.yyyy", Locale("ru"))
        val calendar = GregorianCalendar.getInstance()
        calendar.time = sdf.parse(date)

        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)

        return when (dayOfWeek) {
            Calendar.MONDAY -> "Пн."
            Calendar.TUESDAY -> "Вт."
            Calendar.WEDNESDAY -> "Ср."
            Calendar.THURSDAY -> "Чт."
            Calendar.FRIDAY -> "Пт."
            Calendar.SATURDAY -> "Сб."
            Calendar.SUNDAY -> "Вс."
            else -> throw IllegalArgumentException("Некорректный день недели")
        }
    }

    fun addTestData(context: Context) {
        val currentMonth = LocalDate.now()
        for (i in 1..currentMonth.lengthOfMonth()) {
            val appointmentModelDb = AppointmentModelDb(
                _id = null,
                date = dateConverterNew(LocalDate.now().withDayOfMonth(i).toString()),
                name = generateRandomName(),
                time = "00:00",
                procedure = generateRandomProcedure(),
                phone = generateRandomRussianPhoneNumber(),
                notes = "Заметка",
                deleted = false
            )
            saveAppointment(appointmentModelDb, context)
        }
    }

    private fun generateRandomName(): String {
        val names = listOf(
            "Alice",
            "Bob",
            "Charlie",
            "David",
            "Emma",
            "Frank",
            "Grace",
            "Henry",
            "Ivy",
            "Jack"
        )

        val randomIndex = (names.indices).random()
        return names[randomIndex]
    }

    private fun generateRandomProcedure(): String {
        val names = listOf(
            "Маникюр",
            "Педикюр",
            "Маникюр + покрытие гель-лаком",
            "Укрепление ногтей гелем",
            "Восстановление ногтей",
            "Наращивание ногтей",
            "Дизайн ногтей",
            "SPA-процедура для рук и ногтей"
        )

        val randomIndex = (names.indices).random()
        return names[randomIndex]
    }

    private fun generateRandomRussianPhoneNumber(): String {
        val prefix = "8"
        val randomNumbers = (100_000..999_999).random()
        val randomPart1 = (100..999).random()
        val randomPart2 = (10..99).random()
        val randomPart3 = (10..99).random()
        return "$prefix$randomNumbers$randomPart1$randomPart2$randomPart3"
    }

    private fun saveAppointment(appointmentModelDb: AppointmentModelDb, context: Context): Boolean {
        // Save appointment in database
        val scheduleDb = ScheduleDb.getDb(context = context)
        val thread = Thread {
            scheduleDb.getDao().insert(appointmentModelDb)
        }
        thread.start()
        thread.join()
        Log.e(log, "Appointment $appointmentModelDb saved")
        return true
    }
}