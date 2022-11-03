package com.example.projectnailsschedule

import android.app.Dialog
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.*
import android.widget.AdapterView.OnItemLongClickListener
import androidx.appcompat.app.AppCompatActivity
import com.example.projectnailsschedule.databinding.ActivityDateBinding
import com.example.projectnailsschedule.DataBase.DateStatusDbHelper
import com.example.projectnailsschedule.DataBase.ScheduleDbHelper


class DateActivity : AppCompatActivity() {
    companion object {
        const val COLUMN_START = ScheduleDbHelper.COLUMN_START_TIME
        const val COLUMN_PROCEDURE = ScheduleDbHelper.COLUMN_PROCEDURE
        const val COLUMN_NAME = ScheduleDbHelper.COLUMN_NAME
        const val COLUMN_PHONE = ScheduleDbHelper.COLUMN_PHONE
        const val COLUMN_MISC = ScheduleDbHelper.COLUMN_MISC
        val LOG = this::class.simpleName
        var statusForSpinner = ""
        var dayStatus = "no status"
    }

    private lateinit var binding: ActivityDateBinding
    private var scheduleList: ListView? = null
    private var deleteButton: Button? = null
    private var editButton: Button? = null
    private var dayStatusSpinner: Spinner? = null

    private var databaseHelper: ScheduleDbHelper? = null
    private var db: SQLiteDatabase? = null
    private var dateStatusDbHelper: DateStatusDbHelper? = null
    private var dbStatus: SQLiteDatabase? = null

    private var cursor: Cursor? = null
    private var adapter: SimpleCursorAdapter? = null

    private var day: String? = null
    private var statusMap: Map<String, String> = createStatusMap()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_date)
        Log.e(LOG, "onCreate")
        binding = ActivityDateBinding.inflate(layoutInflater)

        // Получаем интент с датой
        day = intent.getStringExtra("day").toString()

        // Конвертируем дату в формат dd.MM.yyyy
        day = Converter().dateConverter(day!!)

        // Устанавливаем в Toolbar дату
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        toolbar.title = day
        setSupportActionBar(toolbar)

        // Получаем статус дня и устанавливаем в спиннер
        dayStatusSpinner = findViewById(R.id.spinner_status)
        getDayStatus()
        setStatusInSpinner()

        scheduleList = findViewById(R.id.scheduleListView)
        databaseHelper = ScheduleDbHelper(applicationContext)
    }

    public override fun onResume() {
        super.onResume()

        // Получаем строку из БД распасания
        currentDayQuery()

        // Устанавливаем полученные строки в ListView
        setDayQuery()

        // Добавляем ClickListener к ListView расписания
        scheduleList!!.onItemLongClickListener =
            OnItemLongClickListener { _, _, position, _ ->
                // Выводим диалоговое окно на экран
                // Создаем курсор для управления выбранным пунктом ListView
                val currentCur: Cursor = scheduleList!!.getItemAtPosition(position) as Cursor
                showDialog(currentCur)
                true
            }

        // Добавляем ClickListener на Spinner со статусами дня
        dayStatusSpinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
                for ((i, value) in statusMap.values.withIndex()) {
                    if (i == position) {
                        Toast.makeText(
                            applicationContext,
                            "Установлен статус $value",
                            Toast.LENGTH_SHORT
                        ).show()
                        if (value != statusForSpinner) {
                            // Если статус изменился относительно актуального
                            setDayStatus(value)
                        }
                    }
                }
            }
        }
    }

    private fun showDialog(currentCur: Cursor) {
        // Метод регулирует работу диалогового окна Удалить/Редактировать
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog_db)
        deleteButton = dialog.findViewById(R.id.delete)
        editButton = dialog.findViewById(R.id.edit)

        // Получаем id строки для дальнейшего изменения/удаления
        val currentId = currentCur.getInt(0)
        Log.e(LOG, "id selected: $currentCur")

        deleteButton?.setOnClickListener {
            databaseHelper?.deleteRow(currentId, db!!)
            currentDayQuery()
            setDayQuery()
            dialog.dismiss()
        }

        editButton?.setOnClickListener {
            val extraList = arrayListOf(
                currentCur.getString(0).toString(),
                currentCur.getString(1).toString(),
                currentCur.getString(2).toString(),
                currentCur.getString(3).toString(),
                currentCur.getString(4).toString(),
                currentCur.getString(5).toString(),
                currentCur.getString(6).toString(),
            )
            val appointmentIntent = Intent(this, Appointment::class.java)
            appointmentIntent.putExtra("appointmentExtra", extraList)
            startActivity(appointmentIntent)
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun currentDayQuery() {
        // Метод открывает подключение к БД, получает курсор и добавляет его в адаптер

        // Окрываем подключение
        db = databaseHelper?.readableDatabase

        // Получаем курсор с данными из БД по выбранной дате
        cursor = databaseHelper?.fetchRow(day!!, db!!)

        // Определяем, какие столбцы из курсора будут выводиться в ListView
        val headers =
            arrayOf(COLUMN_START, COLUMN_PROCEDURE, COLUMN_NAME, COLUMN_PHONE, COLUMN_MISC)

        // Определяем список элементов, которые будут получать данные из курсова
        val receiver = intArrayOf(
            R.id.appointment_start,
            R.id.appointment_procedure,
            R.id.appointment_name,
            R.id.appointment_phone,
            R.id.appointment_misc
        )

        // Создаем адаптер для ListView, передаем в него курсор
        adapter = SimpleCursorAdapter(
            this, R.layout.database,
            cursor, headers, receiver, 0
        )

        scheduleList!!.adapter = adapter

        Log.e(LOG, "Адаптер с курсором установлен")

    }

    private fun setDayQuery() {
        // Устанавливаем полученную строку в ListView
        scheduleList!!.adapter = adapter
    }

    fun buttonAdd(view: View) {
        // Запустить активность по добавлению строки в БД
        val appointmentIntent = Intent(this, Appointment::class.java)
        appointmentIntent.putExtra("appointmentExtra", day)
        startActivity(appointmentIntent)
        // TODO: переписать через кликлистенер
    }

    private fun getDayStatus() {
        // Получаем из БД статус выбранного дня
        dateStatusDbHelper = DateStatusDbHelper(this)
        dbStatus = dateStatusDbHelper?.readableDatabase
        cursor = dateStatusDbHelper?.fetchDate(day!!, dbStatus!!)

        // Получаем статус дня из курсора
        // Если не удалось подтянуть статус - установить "no status"
        if (cursor!!.moveToFirst()) {
            val columnIndex = cursor!!.getColumnIndex("status")
            dayStatus = cursor!!.getString(columnIndex)
            Log.e(LOG, "Day $day status fetched: ${DateStatusDbHelper.COLUMN_STATUS}")
        } else {
            Log.e(LOG, "Cannot fetch status, no data")
            dayStatus = "no status"
            statusForSpinner = "Свободен"
        }
        cursor?.close()
    }

    private fun setStatusInSpinner() {
        // Устанавливаем статус дня в Spinner
        // Создаем адаптер из массива статусов
        val adapterStatus = ArrayAdapter.createFromResource(
            this,
            R.array.day_status,
            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item
        )
        adapterStatus.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item)

        // В зависимости от статуса устанавливаем значение переменной
        with(DateStatusDbHelper) {
            when (dayStatus) {
                STATUS_FREE -> statusForSpinner = statusMap[STATUS_FREE].toString()
                STATUS_MEDIUM -> statusForSpinner = statusMap[STATUS_MEDIUM].toString()
                STATUS_BUSY -> statusForSpinner = statusMap[STATUS_BUSY].toString()
                STATUS_DAY_OFF -> statusForSpinner = statusMap[STATUS_DAY_OFF].toString()
            }
        }

        // Найти в адаптере позицию выбранного статуса и установить статус в Spinner
        val spinnerPosition = adapterStatus.getPosition(statusForSpinner)
        dayStatusSpinner!!.adapter = adapterStatus
        dayStatusSpinner!!.setSelection(spinnerPosition, true)
        Log.e(LOG, "Status $statusForSpinner ($dayStatus) set")
    }

    private fun setDayStatus(status: String) {
        // Метод устанавливает новый статус в БД
        // Если запись уже существует в БД - обновляет в ней статус
        // Если записи еще нет - создает запись с выбранным статусом

        // Если данного дня нет в БД - создаем запись
        // Если запись уже была - обновляем существующую
        db = dateStatusDbHelper?.writableDatabase
        val dbStatus = statusMap.entries.find { it.value == status }!!.key
        if (dayStatus != "no status") {
            dateStatusDbHelper?.updateDate(day!!, dbStatus, db!!)
        } else {
            dateStatusDbHelper?.addDate(day!!, dbStatus, db!!)
        }
        dateStatusDbHelper?.close()
    }

    private fun createStatusMap(): Map<String, String> {
        // Метод наполняяет словарь для соотношения статуса в БД и статуса в UI
        with(DateStatusDbHelper) {
            return mapOf(
                STATUS_FREE to "Свободен",
                STATUS_MEDIUM to "Есть записи",
                STATUS_BUSY to "Занят",
                STATUS_DAY_OFF to "Выходной",
            )
        }
    }

    public override fun onDestroy() {
        super.onDestroy()
        Log.e(LOG, "onDestroy")
        // Закрываем подключение и курсор
        Log.e(LOG, "Подключение закрыто")
        db!!.close()
        cursor!!.close()
    }
}

