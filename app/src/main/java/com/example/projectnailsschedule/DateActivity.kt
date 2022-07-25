package com.example.projectnailsschedule

import android.annotation.SuppressLint
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
import com.example.projectnailsschedule.dateStatusDB.DateStatusDbHelper
import java.text.SimpleDateFormat


class DateActivity : AppCompatActivity() {
    companion object {
        const val COLUMN_START = DatabaseHelper.COLUMN_START
        const val COLUMN_PROCEDURE = DatabaseHelper.COLUMN_PROCEDURE
        const val COLUMN_NAME = DatabaseHelper.COLUMN_NAME
        const val COLUMN_PHONE = DatabaseHelper.COLUMN_PHONE
        const val COLUMN_MISC = DatabaseHelper.COLUMN_MISC
        const val LOG_NAME = "DateActivity"

    }

    private lateinit var binding: ActivityDateBinding
    private var scheduleList: ListView? = null
    private var deleteButton: Button? = null
    private var editButton: Button? = null
    private var spinner: Spinner? = null

    private var databaseHelper: DatabaseHelper? = null
    private var db: SQLiteDatabase? = null
    private var dateStatusDbHelper: DateStatusDbHelper? = null
    private var dbStatus: SQLiteDatabase? = null

    private var cursor: Cursor? = null
    private var adapter: SimpleCursorAdapter? = null

    private var day: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_date)
        Log.e(LOG_NAME, "onCreate")
        binding = ActivityDateBinding.inflate(layoutInflater)

        // Получаем интент с датой
        day = intent.getStringExtra("day").toString()

        // Конверитуем дату в формат dd.MM.yyyy
        day = dateConverter(day!!)

        // Устанавливаем в Toolbar дату
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        toolbar.title = day
        setSupportActionBar(toolbar)

        // Получаем статус дня и устанавливаем в спиннер
        spinner = findViewById(R.id.spinner_status)
        setDayStatus()

        scheduleList = findViewById(R.id.scheduleListView)
        databaseHelper = DatabaseHelper(applicationContext)
    }

    public override fun onResume() {
        super.onResume()

        // Получаем строку из БД распасания
        currentDayQuery()
        // Устанавливаем полученную строку в ListView
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
    }

    private fun showDialog(currentCur: Cursor) {
        // Метод регулирует работу диалогового окна
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog_db)
        deleteButton = dialog.findViewById(R.id.delete)
        editButton = dialog.findViewById(R.id.edit)

        // Получаем id строки для дальнейшего изменения/удаления
        val currentId = currentCur.getInt(0)
        Log.e(LOG_NAME, "id selected: $currentCur")

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

        Log.e(LOG_NAME, "Адаптер с курсором установлен")

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

    @SuppressLint("SimpleDateFormat")
    fun dateConverter(day: String): String {
        // Получаем день из интента в формате d.M.yyyy и конвертируем в формат dd.MM.yyyy
        val parser = SimpleDateFormat("d.M.yyyy")
        val formatter = SimpleDateFormat("dd.MM.yyyy")
        return formatter.format(parser.parse(day)).toString()
    }

    private fun setDayStatus() {
        // Получаем из БД статус выбранного дня и устанавливаем его в Spinner
        var dayStatus = "no status"
        var compareValue = ""
        dateStatusDbHelper = DateStatusDbHelper(this)
        dbStatus = dateStatusDbHelper?.readableDatabase
        cursor = dateStatusDbHelper?.fetchDate(day!!, dbStatus!!)

        // Получаем статус дня из курсора
        if (cursor!!.moveToFirst()) {
            val columnIndex = cursor!!.getColumnIndex("status")
            dayStatus = cursor!!.getString(columnIndex)
            Log.e(LOG_NAME, "Day $day status fetched: ${DateStatusDbHelper.COLUMN_STATUS}")
        } else {
            Log.e(LOG_NAME, "Cannot fetch status, no data")
            compareValue = "Свободен"
        }
        cursor?.close()

        // Создаем адаптер из массива статусов
        val adapterStatus = ArrayAdapter.createFromResource(
            this,
            R.array.day_status,
            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item
        )
        adapterStatus.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item)

        // В зависимости от статуса устанавливаем значение переменную
        with(DateStatusDbHelper) {
            when (dayStatus) {
                STATUS_FREE -> compareValue = "Свободен"
                STATUS_MEDIUM -> compareValue = "Есть записи"
                STATUS_BUSY -> compareValue = "Занят"
                STATUS_DAY_OFF -> compareValue = "Выходной"
            }
        }

        // Найти в адаптере позицию выбранного статуса и установить статус в Spinner
        val spinnerPosition = adapterStatus.getPosition(compareValue)
        spinner!!.adapter = adapterStatus
        spinner!!.setSelection(spinnerPosition, true)
        Log.e(LOG_NAME, "Status $compareValue ($dayStatus) set")
    }

    public override fun onDestroy() {
        super.onDestroy()
        Log.e(LOG_NAME, "onDestroy")
        // Закрываем подключение и курсор
        Log.e(LOG_NAME, "Подключение закрыто")
        db!!.close()
        cursor!!.close()
    }
}

