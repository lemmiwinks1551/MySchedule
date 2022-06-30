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
import android.widget.AdapterView.OnItemLongClickListener
import android.widget.Button
import android.widget.ListView
import android.widget.SimpleCursorAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat


class DateActivity : AppCompatActivity() {
    companion object {
        const val COLUMN_ID = DatabaseHelper.COLUMN_ID
        const val COLUMN_START = DatabaseHelper.COLUMN_START
        const val COLUMN_PROCEDURE = DatabaseHelper.COLUMN_PROCEDURE
        const val COLUMN_NAME = DatabaseHelper.COLUMN_NAME
        const val COLUMN_PHONE = DatabaseHelper.COLUMN_PHONE
        const val COLUMN_MISC = DatabaseHelper.COLUMN_MISC
        const val COLUMN_DATE = DatabaseHelper.COLUMN_DATE
        const val LOG_NAME = "DateActivity"
    }

    private var scheduleList: ListView? = null
    private var databaseHelper: DatabaseHelper? = null
    private var db: SQLiteDatabase? = null
    private var cursor: Cursor? = null
    private var adapter: SimpleCursorAdapter? = null
    private var deleteButton: Button? = null
    private var editButton: Button? = null
    private var day: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_date)
        Log.e(LOG_NAME, "onCreate")

        // Получаем интент с датой
        day = intent.getStringExtra("day").toString()

        // Конверитуем дату в удобный формат
        day = dateConverter(day!!)

        // Устанавливаем в Toolbar дату
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        toolbar.title = day
        setSupportActionBar(toolbar)

        scheduleList = findViewById(R.id.scheduleListView)
        databaseHelper = DatabaseHelper(applicationContext)
    }

    public override fun onResume() {
        super.onResume()
        // Получаем строку из БД
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
            deleteId(currentId)
            currentDayQuery()
            setDayQuery()
            dialog.dismiss()
        }

        editButton?.setOnClickListener {
            editId(currentId)
            currentDayQuery()
            setDayQuery()
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun currentDayQuery() {
        // Функция получает из БД строчку в зависимости от дня записи.
        Log.e(LOG_NAME, "Открываем подключение")

        // Окрываем подключение
        db = databaseHelper?.readableDatabase

        // Формируем запрос к БД
        val query = "SELECT * FROM ${DatabaseHelper.TABLE_NAME} WHERE $COLUMN_DATE = '$day';"

        // Получаем данные из бд в виде курсора
        cursor = db!!.rawQuery(query, null)

        // Определяем, какие столбцы из курсора будут выводиться в ListView
        val headers =
            arrayOf(COLUMN_START, COLUMN_PROCEDURE, COLUMN_NAME, COLUMN_PHONE, COLUMN_MISC)
        // Определяем список элементов, которые будут заполнять
        val receiver = intArrayOf(
            R.id.appointment_start,
            R.id.appointment_procedure,
            R.id.appointment_name,
            R.id.appointment_phone,
            R.id.appointment_misc
        )

        // Создаем адаптер, передаем в него курсор
        adapter = SimpleCursorAdapter(
            this, R.layout.database,
            cursor, headers, receiver, 0
        )
    }

    private fun setDayQuery() {
        // Устанавливаем полученную строку в ListView
        scheduleList!!.adapter = adapter
    }

    public override fun onDestroy() {
        super.onDestroy()
        Log.e(LOG_NAME, "onDestroy")
        // Закрываем подключение и курсор
        Log.e(LOG_NAME, "Подключение закрыто")
        db!!.close()
        cursor!!.close()
    }

    private fun deleteId(currentId: Int) {
        db!!.execSQL("DELETE FROM ${DatabaseHelper.TABLE_NAME} WHERE $COLUMN_ID = $currentId;")
        Log.e(LOG_NAME, String.format("Row № $currentId deleted"))
    }

    private fun editId(currentId: Int) {
        Log.e(LOG_NAME, String.format("Row № $currentId edited"))
    }

    fun buttonAdd(view: View) {
        val addAppointmentIntent = Intent(this, AddAppointment::class.java)
        addAppointmentIntent.putExtra("date", day)
        startActivity(addAppointmentIntent)
    }

    @SuppressLint("SimpleDateFormat")
    private fun dateConverter(day: String): String {
        // Получаем день из интента в формате d.M.yyyy и конвертируем в формат dd.MM.yyyy
        val parser = SimpleDateFormat("d.M.yyyy")
        val formatter = SimpleDateFormat("dd.MM.yyyy")
        return formatter.format(parser.parse(day)).toString()
    }
}

