package com.example.projectnailsschedule.util

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.graphics.Color
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import com.example.projectnailsschedule.R
import com.example.projectnailsschedule.data.storage.ClientsDb
import com.example.projectnailsschedule.data.storage.ProceduresDb
import com.example.projectnailsschedule.data.storage.ScheduleDb
import com.example.projectnailsschedule.domain.models.AppointmentModelDb
import com.example.projectnailsschedule.domain.models.ClientModelDb
import com.example.projectnailsschedule.domain.models.ProcedureModelDb
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.time.temporal.WeekFields
import java.util.*

/**
 * Вспомогательный класс
 * */

class Util {

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

    fun getDayOfWeek(date: String, context: Context): String {
        val sdf = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        val calendar = Calendar.getInstance(Locale.getDefault())
        calendar.time = sdf.parse(date) as Date

        val dayOfWeekResource = when (calendar.get(Calendar.DAY_OF_WEEK)) {
            Calendar.MONDAY -> R.string.mon
            Calendar.TUESDAY -> R.string.tue
            Calendar.WEDNESDAY -> R.string.wed
            Calendar.THURSDAY -> R.string.thu
            Calendar.FRIDAY -> R.string.fri
            Calendar.SATURDAY -> R.string.sat
            Calendar.SUNDAY -> R.string.sun
            else -> 0
        }

        return context.getString(dayOfWeekResource)
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
                telegram = generateRandomRussianPhoneNumber(),
                instagram = generateRandomInstagramLink(),
                vk = generateRandomVkLink(),
                whatsapp = generateRandomRussianPhoneNumber(),
                notes = "Заметка",
                deleted = false
            )
            saveAppointment(appointmentModelDb, context)
        }
    }

    suspend fun createTestClients(context: Context) {
        val clientsDb = ClientsDb.getDb(context)
        CoroutineScope(Dispatchers.IO).launch {
            repeat(50) {
                val testClient = ClientModelDb(
                    _id = null,
                    name = generateRandomName(),
                    phone = generateRandomRussianPhoneNumber(),
                    telegram = generateRandomRussianPhoneNumber(),
                    instagram = generateRandomInstagramLink(),
                    vk = generateRandomVkLink(),
                    whatsapp = generateRandomRussianPhoneNumber(),
                    notes = null
                )
                clientsDb.getDao().insert(testClient)
            }
        }
        clientsDb.close()
    }

    suspend fun createTestProcedures(context: Context) = withContext(Dispatchers.IO) {
        val proceduresDb = ProceduresDb.getDb(context)

        repeat(25) {
            val testProcedure = ProcedureModelDb(
                _id = null,
                procedureName = generateRandomProcedure(),
                procedurePrice = kotlin.random.Random.nextInt(500, 3001).toString(),
                procedureNotes = generateRandomNote()
            )
            proceduresDb.getDao().insert(testProcedure)
        }
        proceduresDb.close()
    }

    private fun generateRandomNote(): String {
        val quotes = listOf(
            "Всё идёт по плану.",
            "Время лечит раны.",
            "Любовь спасёт мир.",
            "Человек — король природы.",
            "Береги платье снову, а честь смолоду.",
            "Счастливое детство — залог счастья в будущем.",
            "Терпение и труд всё перетрут.",
            "В каждой шутке есть доля правды.",
            "Друзья познаются в беде.",
            "Жизнь прекрасна во всех своих проявлениях."
        )

        return quotes.random()

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

    private fun generateRandomInstagramLink(): String {
        val characters = "abcdefghijklmnopqrstuvwxyz1234567890"
        val linkLength = 11  // Длина случайной ссылки в Instagram

        val random = Random()
        val linkBuilder = StringBuilder(linkLength)

        for (i in 0 until linkLength) {
            val randomIndex = random.nextInt(characters.length)
            linkBuilder.append(characters[randomIndex])
        }

        return "https://www.instagram.com/$linkBuilder/"
    }

    private fun generateRandomVkLink(): String {
        val characters = "abcdefghijklmnopqrstuvwxyz1234567890"
        val linkLength = 10  // Длина случайной ссылки в VK

        val random = Random()
        val linkBuilder = StringBuilder(linkLength)

        for (i in 0 until linkLength) {
            val randomIndex = random.nextInt(characters.length)
            linkBuilder.append(characters[randomIndex])
        }

        return "https://vk.com/$linkBuilder"
    }

    private fun saveAppointment(appointmentModelDb: AppointmentModelDb, context: Context): Boolean {
        // Save appointment in database
        val scheduleDb = ScheduleDb.getDb(context = context)
        CoroutineScope(Dispatchers.IO).launch {
            scheduleDb.getDao().insert(appointmentModelDb)
        }
        Log.e(log, "Appointment $appointmentModelDb saved")
        scheduleDb.close()
        return true
    }

    fun animateEditTexts(vararg editTexts: EditText) {
        for (editText in editTexts) {
            val originalColor = editText.currentTextColor

            editText.setTextColor(Color.RED)

            val animator = ValueAnimator.ofArgb(Color.RED, originalColor)
            animator.duration = 2000

            animator.addUpdateListener { valueAnimator ->
                val animatedValue = valueAnimator.animatedValue as Int
                editText.setTextColor(animatedValue)
            }

            animator.start()
        }
    }

    fun clearDir(dir: File) {
        val files = dir.listFiles()
        if (files != null) {
            for (file in files) {
                if (file.isDirectory) {
                    clearDir(file) // Рекурсивно очищаем поддиректории
                } else {
                    file.delete() // Удаляем файл
                }
            }
        }
    }

    fun isDarkModeEnabled(context: Context): Boolean {
        val currentNightMode = context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return currentNightMode == Configuration.UI_MODE_NIGHT_YES
    }

    fun formatDateToRus(date: LocalDate): String {
        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy", Locale("ru"))
        return date.format(formatter)
    }
}