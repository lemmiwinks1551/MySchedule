package com.example.projectnailsschedule.presentation.date

import android.app.Dialog
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import android.widget.AdapterView.OnItemLongClickListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.projectnailsschedule.R
import com.example.projectnailsschedule.data.storage.StatusDbHelper
import com.example.projectnailsschedule.data.storage.ScheduleDbHelper
import com.example.projectnailsschedule.databinding.FragmentDateBinding
import com.example.projectnailsschedule.domain.models.AppointmentParams
import com.example.projectnailsschedule.domain.models.DateParams
import com.example.projectnailsschedule.util.Util
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*


class DateFragment : Fragment() {
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

    private var _binding: FragmentDateBinding? = null
    private val binding get() = _binding!!
    private val bindingKey = "dateParams"

    private var scheduleList: ListView? = null
    private var deleteButton: Button? = null
    private var editButton: Button? = null
    private var dayStatusSpinner: Spinner? = null

    private var databaseHelper: ScheduleDbHelper? = null
    private var db: SQLiteDatabase? = null
    private var statusDbHelper: StatusDbHelper? = null
    private var dbStatus: SQLiteDatabase? = null

    private var cursor: Cursor? = null
    private var adapter: SimpleCursorAdapter? = null

    private var day: String? = null
    private var statusMap: Map<String, String> = createStatusMap()

    private var chosenDate: LocalDate? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        val dateViewModel =
            ViewModelProvider(this)[DateViewModel::class.java]

        _binding = FragmentDateBinding.inflate(inflater, container, false)

        // Get selected date from bundle
        val dateParams: DateParams? = arguments?.getParcelable(bindingKey)
        if (dateParams != null) {
            day = dateParams.date?.dayOfMonth.toString()
            chosenDate = dateParams.date
        }

        // Конвертируем дату в формат dd.MM.yyyy
        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
        if (dateParams != null) {
            day = dateParams.date!!.format(formatter)!!
        }

        // Получаем статус дня и устанавливаем в спиннер
        dayStatusSpinner = binding.spinnerStatus
        getDayStatus()
        setStatusInSpinner()

        scheduleList = binding.scheduleListView
        databaseHelper = ScheduleDbHelper(context)

        //Вызываем новый фрагмент для добавления новой записи
        binding.addButton.setOnClickListener {
            // Add new appointment, send date
            val appointmentParams = AppointmentParams(
                _id = null,
                appointmentDate = day,
                clientName = null,
                startTime = null,
                procedureName = null,
                phoneNum = null,
                misc = null
            )
            val bundle = Bundle()
            bundle.putParcelable("appointmentParams", appointmentParams)
            it.findNavController().navigate(R.id.action_dateFragment_to_appointmentFragment, bundle)
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()

        // Hide keyboard
        Util().hideKeyboard(requireActivity())

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
                showDialog(currentCur, findNavController())
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
                            context,
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

    private fun showDialog(currentCur: Cursor, navController: NavController) {
        // Метод регулирует работу диалогового окна Удалить/Редактировать
        val dialog = Dialog(requireContext())
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
            // Передаем в интенте значение полей, если нажали кнопку "Редактировать"
            val appointmentParams = AppointmentParams(
                _id = currentCur.getString(0).toInt(),
                appointmentDate = currentCur.getString(1).toString(),
                clientName = currentCur.getString(4).toString(),
                startTime = currentCur.getString(2).toString(),
                procedureName = currentCur.getString(3).toString(),
                phoneNum = currentCur.getString(5).toString(),
                misc = currentCur.getString(6).toString()
            )
            val bundle = Bundle()
            bundle.putParcelable("appointmentParams", appointmentParams)

            navController.navigate(R.id.action_dateFragment_to_appointmentFragment, bundle)
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun currentDayQuery() {
        // Метод открывает подключение к БД, получает курсор и добавляет его в адаптер

        // Окрываем подключение
        db = databaseHelper?.readableDatabase

        // Получаем курсор с данными из БД по выбранной дате
        cursor = databaseHelper?.getRow(day!!, db!!)

        // Определяем, какие столбцы из курсора будут выводиться в ListView
        val headers =
            arrayOf(COLUMN_START, COLUMN_PROCEDURE, COLUMN_NAME, COLUMN_PHONE, COLUMN_MISC)

        // Определяем список элементов, которые будут получать данные из курсова
        val receiver = intArrayOf(
            R.id.database_start,
            R.id.database_procedure,
            R.id.database_name,
            R.id.database_phone,
            R.id.database_misc
        )

        // Создаем адаптер для ListView, передаем в него курсор
        adapter = SimpleCursorAdapter(
            context, R.layout.database,
            cursor, headers, receiver, 0
        )

        Log.e(LOG, "Адаптер с курсором установлен")

    }

    private fun setDayQuery() {
        // Устанавливаем полученную строку в ListView
        scheduleList!!.adapter = adapter
    }

    private fun getDayStatus() {
        // Получаем из БД статус выбранного дня
        statusDbHelper = StatusDbHelper(context)
        dbStatus = statusDbHelper?.readableDatabase

        val dateParams = DateParams(_id = null, date = chosenDate, status = null)
        cursor = statusDbHelper?.getDate(dateParams, dbStatus!!)

        // Получаем статус дня из курсора
        // Если не удалось подтянуть статус - установить "no status"
        if (cursor!!.moveToFirst()) {
            val columnIndex = cursor!!.getColumnIndex("status")
            dayStatus = cursor!!.getString(columnIndex)
            Log.e(LOG, "Day $day status fetched: ${StatusDbHelper.COLUMN_STATUS}")
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
            requireContext(),
            R.array.day_status,
            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item
        )
        adapterStatus.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item)

        // В зависимости от статуса устанавливаем значение переменной
        with(StatusDbHelper) {
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
        val dateParams = DateParams(_id = null, date = chosenDate, status = status)
        db = statusDbHelper?.writableDatabase
        statusDbHelper?.addDate(dateParams, db!!)
        statusDbHelper?.setStatus(dateParams, db!!)

        statusDbHelper?.close()
    }

    private fun createStatusMap(): Map<String, String> {
        // Метод наполняяет словарь для соотношения статуса в БД и статуса в UI
        with(StatusDbHelper) {
            return mapOf(
                STATUS_FREE to "Свободен",
                STATUS_MEDIUM to "Есть записи",
                STATUS_BUSY to "Занят",
                STATUS_DAY_OFF to "Выходной",
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e(LOG, "onDestroy")
        // Закрываем подключение и курсор
        Log.e(LOG, "Подключение закрыто")
        db!!.close()
        cursor!!.close()
    }
}
