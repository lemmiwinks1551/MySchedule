package com.example.projectnailsschedule.presentation.calendar.calendarRecyclerView

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.projectnailsschedule.R
import com.example.projectnailsschedule.domain.models.CalendarDateModelDb
import com.example.projectnailsschedule.domain.models.DateParams
import com.example.projectnailsschedule.presentation.calendar.DateParamsViewModel
import com.example.projectnailsschedule.util.Util
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate

class CalendarRvAdapter(
    private val daysInMonth: ArrayList<String>,
    private val dateParamsViewModel: DateParamsViewModel,
    private val context: Context
) : RecyclerView.Adapter<CalendarRvAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        lateinit var date: TextView
        lateinit var dateAppointmentsCount: TextView
        lateinit var cellLayout: ConstraintLayout
        lateinit var dayOffIcon: ImageView
        lateinit var progressBar: ProgressBar

        init {
            inflateViews()
        }

        private fun inflateViews() {
            date = itemView.findViewById(R.id.date_cell)
            dateAppointmentsCount = itemView.findViewById(R.id.date_appointments_text_view)
            cellLayout = itemView.findViewById(R.id.calendarRecyclerViewCell)
            dayOffIcon = itemView.findViewById(R.id.day_off_icon)
        }
    }

    private lateinit var greenImageView: ImageView
    private lateinit var yellowImageView: ImageView
    private lateinit var redImageView: ImageView
    private lateinit var blueImageView: ImageView
    private lateinit var resetImageView: ImageView

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.calendar_rv_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // get day to work with
        val dayInHolder = daysInMonth[position]

        // set day number in CalendarViewHolder
        holder.date.text = dayInHolder

        // set appointments count
        if (dayInHolder != "") {
            // get appointment count from date
            val selectedDate = DateParams(
                date = dateParamsViewModel.selectedDate.value?.date?.withDayOfMonth(
                    dayInHolder.toInt()
                )
            )
            val ruFormatDate = Util().formatDateToRus(selectedDate.date!!)

            // get date appointments
            CoroutineScope(Dispatchers.IO).launch {
                selectedDate.appointmentsList =
                    dateParamsViewModel.getArrayAppointments(date = selectedDate.date!!)
                withContext(Dispatchers.Main) {
                    // set appointments size into holder
                    if (selectedDate.appointmentsList!!.isNotEmpty()) {
                        holder.dateAppointmentsCount.text =
                            selectedDate.appointmentsList!!.size.toString()
                    }
                }
            }

            // get date color from database
            CoroutineScope(Dispatchers.IO).launch {
                val dateColor = dateParamsViewModel.getDateColor(ruFormatDate)
                withContext(Dispatchers.Main) {
                    if (dateColor != null) {
                        // set color to holder
                        holder.cellLayout.setBackgroundResource(mapColorToDrawable(dateColor))
                    }
                }
            }

            setOnCalendarClickListener(holder, selectedDate)

            setOnCalendarLongClickListener(holder, ruFormatDate)

            // If the day corresponds to today's date, set the text color to red
            setCurrentDateRedColor(holder, selectedDate)

            // Day off
            CoroutineScope(Dispatchers.IO).launch {
                val dayNum = Util().getDayOfYear(ruFormatDate) - 1
                val dayOffStatus = dateParamsViewModel.getDataInfo(context, dayNum).type_id
                if (dayOffStatus == 3 || dayOffStatus == 6) {
                    Log.i("DayOffStatus", "$ruFormatDate is Day-off")
                    withContext(Dispatchers.Main) {
                        holder.dayOffIcon.visibility = View.VISIBLE

                        // set icon to holder
                        val notes = dateParamsViewModel.getDataInfo(context, dayNum).note
                        val icon = dateParamsViewModel.getHolidayIcon(notes)
                        holder.dayOffIcon.setImageResource(icon)
                    }
                } else {
                    Log.i("DayOffStatus", "$ruFormatDate is not Day-off")
                }
            }

            restoreSelection(holder)
        }
    }

    override fun getItemCount(): Int {
        return daysInMonth.size
    }

    private fun setDateColorDialog(holder: ViewHolder, ruFormatDate: String) {
        // Start scale up animation
        val scaleUpAnimation = AnimationUtils.loadAnimation(context, R.anim.scale_up)
        holder.cellLayout.startAnimation(scaleUpAnimation)

        val builder = AlertDialog.Builder(context)
        val inflater = LayoutInflater.from(context)
        val dialogView = inflater.inflate(R.layout.custom_dialog, null)
        builder.setView(dialogView)

        // Add date to the dialog title
        val dateColorDialogTitle = dialogView.findViewById<TextView>(R.id.DateColorDialogTitle)
        dateColorDialogTitle.text =
            context.getString(R.string.formatted_date, ruFormatDate)

        val dialog = builder.create()
        val layoutParams = WindowManager.LayoutParams()

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialog.window?.attributes = layoutParams

        dialog.show()

        setColorsClickListeners(holder, dialogView, dialog, ruFormatDate)
    }

    private fun setColorsClickListeners(
        holder: ViewHolder,
        dialogView: View,
        dialog: AlertDialog,
        ruFormatDate: String
    ) {
        initColorsImageButtons(dialogView)

        greenImageView.setOnClickListener {
            val color = "green"
            paintViewHolder(holder, color)

            CoroutineScope(Dispatchers.IO).launch {
                changeColorInDb(ruFormatDate = ruFormatDate, color = color)
            }
            dialog.dismiss()
        }

        yellowImageView.setOnClickListener {
            val color = "yellow"
            paintViewHolder(holder, color)

            CoroutineScope(Dispatchers.IO).launch {
                changeColorInDb(ruFormatDate = ruFormatDate, color = color)
            }
            dialog.dismiss()
        }

        redImageView.setOnClickListener {
            val color = "red"
            paintViewHolder(holder, color)

            CoroutineScope(Dispatchers.IO).launch {
                changeColorInDb(ruFormatDate = ruFormatDate, color = color)
            }
            dialog.dismiss()
        }

        blueImageView.setOnClickListener {
            val color = "blue"
            paintViewHolder(holder, color)

            CoroutineScope(Dispatchers.IO).launch {
                changeColorInDb(ruFormatDate = ruFormatDate, color = color)
            }
            dialog.dismiss()
        }

        resetImageView.setOnClickListener {
            val color = "default"
            paintViewHolder(holder, color)

            CoroutineScope(Dispatchers.IO).launch {
                changeColorInDb(ruFormatDate = ruFormatDate, color = color)
            }
            dialog.dismiss()
        }
    }

    private suspend fun changeColorInDb(ruFormatDate: String, color: String) {
        Log.d("Color", "Changing color for $ruFormatDate")
        val id = dateParamsViewModel.getDateId(ruFormatDate = ruFormatDate)

        if (id == null) {
            // if date in not exists in database
            insertDateWithNewColor(ruFormatDate = ruFormatDate, color = color)
        } else {
            if (color == "default") {
                // if color is deleted - delete from db
                calendarDbDeleteObj(id = id)
            } else {
                // if date already exists in database
                replaceColor(id = id, ruFormatDate = ruFormatDate, color = color)
            }
        }
    }

    private suspend fun insertDateWithNewColor(ruFormatDate: String, color: String) {
        Log.d("Color", "Insert color for date $ruFormatDate")
        val calendarDateModelDb = CalendarDateModelDb(
            date = ruFormatDate,
            color = color
        )
        insertColorToCalendarDb(calendarDateModelDb = calendarDateModelDb)
    }

    private suspend fun replaceColor(id: Int, ruFormatDate: String, color: String) {
        Log.e("Color", "Replacing color for date $ruFormatDate")
        val calendarDateModelDb = CalendarDateModelDb(
            _id = id,
            date = ruFormatDate,
            color = color
        )
        insertColorToCalendarDb(calendarDateModelDb = calendarDateModelDb)
    }

    private suspend fun calendarDbDeleteObj(id: Int) {
        val calendarDbObj = CalendarDateModelDb(_id = id)
        dateParamsViewModel.calendarDbDeleteObj(calendarDbObj)
    }

    private suspend fun insertColorToCalendarDb(calendarDateModelDb: CalendarDateModelDb) {
        dateParamsViewModel.insertCalendarDate(calendarDateModelDb)
    }

    private fun initColorsImageButtons(dialogView: View) {
        greenImageView = dialogView.findViewById(R.id.circleGreen)
        yellowImageView = dialogView.findViewById(R.id.circleYellow)
        redImageView = dialogView.findViewById(R.id.circleRed)
        blueImageView = dialogView.findViewById(R.id.circleBlue)
        resetImageView = dialogView.findViewById(R.id.circleReset)
    }

    private fun paintViewHolder(holder: ViewHolder, color: String) {
        holder.cellLayout.background = AppCompatResources.getDrawable(
            context,
            mapColorToDrawable(color)
        )
    }

    private fun mapColorToDrawable(color: String): Int {
        return when (color) {
            "green" -> R.drawable.calendar_recycler_view_borders_green
            "yellow" -> R.drawable.calendar_recycler_view_borders_yellow
            "red" -> R.drawable.calendar_recycler_view_borders_red
            "blue" -> R.drawable.calendar_recycler_view_borders_blue
            "default" -> R.drawable.calendar_recycler_view_borders
            else -> R.drawable.calendar_recycler_view_borders
        }
    }

    private fun restoreSelection(holder: ViewHolder) {
        if (dateParamsViewModel.dateDetailsVisibility.value!!) {
            val holderDate = holder.date.text
            val prevHolderDate = dateParamsViewModel.prevCalendarRvHolder?.date?.text

            if (holderDate == prevHolderDate) {
                holder.date.setTypeface(null, Typeface.BOLD)

                dateParamsViewModel.prevCalendarRvHolder = holder
            }
        }
    }

    private fun setOnCalendarClickListener(holder: ViewHolder, selectedDate: DateParams) {
        // Set the click listener to handle cell selection
        holder.cellLayout.setOnClickListener {
            if (holder != dateParamsViewModel.prevCalendarRvHolder) {

                unSelectPreviousHolder()

                selectDate(holder)

                dateParamsViewModel.prevCalendarRvHolder = holder

                dateParamsViewModel.updateSelectedDate(
                    dateParams = selectedDate
                )

                //  Day off
                CoroutineScope(Dispatchers.IO).launch {
                    val dayNum =
                        Util().getDayOfYear(Util().formatDateToRus(selectedDate.date!!)) - 1
                    val dayOffStatus = dateParamsViewModel.getDataInfo(context, dayNum)
                    if (dayOffStatus.type_id == 3 || dayOffStatus.type_id == 6) {
                        val typeText = dateParamsViewModel.getDataInfo(context, dayNum).type_text
                        val note = dateParamsViewModel.getDataInfo(context, dayNum).note
                        if (note == null || note == "") {
                            dateParamsViewModel.dayOffInfo.postValue(typeText)
                        } else {
                            dateParamsViewModel.dayOffInfo.postValue(note)
                        }
                    } else {
                        dateParamsViewModel.dayOffInfo.postValue(null)
                    }
                }

                dateParamsViewModel.dateDetailsVisibility.value = true
            }
        }
    }

    private fun setOnCalendarLongClickListener(holder: ViewHolder, ruFormatDate: String) {
        holder.cellLayout.setOnLongClickListener {
            setDateColorDialog(holder, ruFormatDate)
            true
        }
    }

    private fun setCurrentDateRedColor(holder: ViewHolder, selectedDate: DateParams) {
        if (selectedDate.date!! == LocalDate.now()) {
            holder.date.setTextColor(context.resources.getColor(R.color.red_weekend))
        }
    }

    private fun unSelectPreviousHolder() {
        dateParamsViewModel.prevCalendarRvHolder?.date?.setTypeface(null, Typeface.NORMAL)
    }

    private fun selectDate(holder: ViewHolder) {
        // Make selected date bold
        holder.date.setTypeface(null, Typeface.BOLD)
    }
}