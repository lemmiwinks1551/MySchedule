package com.example.projectnailsschedule.presentation.calendar.calendarRecyclerView

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.projectnailsschedule.R
import com.example.projectnailsschedule.domain.models.CalendarDateModelDb
import com.example.projectnailsschedule.domain.models.DateParams
import com.example.projectnailsschedule.presentation.calendar.CalendarViewModel
import com.example.projectnailsschedule.util.Util
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate

class CalendarRvAdapter(
    private val daysInMonth: ArrayList<String>,
    private val calendarViewModel: CalendarViewModel,
    private val context: Context
) : RecyclerView.Adapter<CalendarRvAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        lateinit var date: TextView
        lateinit var dateAppointmentsCount: TextView
        lateinit var cellLayout: ConstraintLayout

        init {
            inflateViews()
        }

        private fun inflateViews() {
            date = itemView.findViewById(R.id.date_cell)
            dateAppointmentsCount = itemView.findViewById(R.id.date_appointments_text_view)
            cellLayout = itemView.findViewById(R.id.calendarRecyclerViewCell)
        }
    }

    private val log = this::class.simpleName
    private var unselectedBackground: Int = R.drawable.calendar_recycler_view_borders

    private lateinit var greenImageView: ImageView
    private lateinit var yellowImageView: ImageView
    private lateinit var redImageView: ImageView
    private lateinit var blueImageView: ImageView
    private lateinit var resetImageView: ImageView

    private var greenBackground: Int = R.drawable.calendar_recycler_view_borders_green
    private var yellowBackground: Int = R.drawable.calendar_recycler_view_borders_yellow
    private var redBackground: Int = R.drawable.calendar_recycler_view_borders_red
    private var blueBackground: Int = R.drawable.calendar_recycler_view_borders_blue
    private var defaultBackground: Int = R.drawable.calendar_recycler_view_borders

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
            val appointmentCount: Int
            val dateParams = DateParams(
                date = calendarViewModel.selectedDate.value?.date?.withDayOfMonth(
                    dayInHolder.toInt()
                )
            )

            // get date appointment count
            appointmentCount =
                calendarViewModel.getArrayAppointments(dateParams = dateParams).size

            Log.e(log, "$dateParams")
            val ruFormatDate = Util().formatDate(dateParams.date!!)

            // If appointments exists
            if (appointmentCount > 0) {
                holder.dateAppointmentsCount.text = appointmentCount.toString()
            }

            // If the day corresponds to today's date, set the text color to red
            if (dateParams.date!! == LocalDate.now()) {
                holder.date.setTextColor(Color.RED)
            }

            // get date color from database
            CoroutineScope(Dispatchers.IO).launch {
                val dateColor = calendarViewModel.getDateColor(ruFormatDate)
                withContext(Dispatchers.Main) {
                    if (dateColor != null) {
                        holder.cellLayout.setBackgroundResource(dateColor)
                    }
                }
            }

            // Set the click listener to handle cell selection
            holder.cellLayout.setOnClickListener {
                if (holder != calendarViewModel.prevHolder) {

                    // Unselect the previously selected cell
                    calendarViewModel.prevHolder?.cellLayout?.setBackgroundResource(
                        unselectedBackground
                    )

                    // Select the clicked cell
                    holder.cellLayout.setBackgroundResource(R.drawable.bold_borders_square)

                    // Update the previous holder
                    calendarViewModel.prevHolder = holder
                }

                calendarViewModel.updateSelectedDate(day = dayInHolder.toInt())
                calendarViewModel.visibility.value = true
            }

            holder.cellLayout.setOnLongClickListener {
                setDateColorDialog(holder, ruFormatDate)
                true
            }
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
            val color = greenBackground
            paintViewHolder(holder, color)

            CoroutineScope(Dispatchers.IO).launch {
                changeColorInDb(ruFormatDate = ruFormatDate, color = color)
            }
            dialog.dismiss()
        }

        yellowImageView.setOnClickListener {
            val color = yellowBackground
            paintViewHolder(holder, color)

            CoroutineScope(Dispatchers.IO).launch {
                changeColorInDb(ruFormatDate = ruFormatDate, color = color)
            }
            dialog.dismiss()
        }

        redImageView.setOnClickListener {
            val color = redBackground
            paintViewHolder(holder, color)

            CoroutineScope(Dispatchers.IO).launch {
                changeColorInDb(ruFormatDate = ruFormatDate, color = color)
            }
            dialog.dismiss()
        }

        blueImageView.setOnClickListener {
            val color = blueBackground
            paintViewHolder(holder, color)

            CoroutineScope(Dispatchers.IO).launch {
                changeColorInDb(ruFormatDate = ruFormatDate, color = color)
            }
            dialog.dismiss()
        }

        resetImageView.setOnClickListener {
            val color = defaultBackground
            paintViewHolder(holder, color)

            CoroutineScope(Dispatchers.IO).launch {
                changeColorInDb(ruFormatDate = ruFormatDate, color = color)
            }
            dialog.dismiss()
        }
    }

    private suspend fun changeColorInDb(ruFormatDate: String, color: Int) {
        Log.d("Color", "Changing color for $ruFormatDate")
        val id = calendarViewModel.getDateId(ruFormatDate = ruFormatDate)
        if (id == null) {
            // if date in not exists in database
            insertDateWithNewColor(ruFormatDate = ruFormatDate, color = color)
        } else {
            // if date already exists in database
            replaceColor(id, ruFormatDate, color)
        }
    }

    private suspend fun insertDateWithNewColor(ruFormatDate: String, color: Int) {
        Log.d("Color", "Insert color for date $ruFormatDate")
        val calendarDateModelDb = CalendarDateModelDb(
            date = ruFormatDate,
            color = color
        )
        insertColorToCalendarDb(calendarDateModelDb = calendarDateModelDb)
    }

    private suspend fun replaceColor(id: Int, ruFormatDate: String, colorBackground: Int) {
        Log.e("Color", "Replacing color for date $ruFormatDate")
        val calendarDateModelDb = CalendarDateModelDb(
            _id = id,
            date = ruFormatDate,
            color = colorBackground
        )
        insertColorToCalendarDb(calendarDateModelDb = calendarDateModelDb)
    }

    private suspend fun insertColorToCalendarDb(calendarDateModelDb: CalendarDateModelDb) {
        calendarViewModel.insertCalendarDate(calendarDateModelDb)
    }

    private fun initColorsImageButtons(dialogView: View) {
        greenImageView = dialogView.findViewById(R.id.circleGreen)
        yellowImageView = dialogView.findViewById(R.id.circleYellow)
        redImageView = dialogView.findViewById(R.id.circleRed)
        blueImageView = dialogView.findViewById(R.id.circleBlue)
        resetImageView = dialogView.findViewById(R.id.circleReset)
    }

    private fun paintViewHolder(holder: ViewHolder, color: Int) {
        holder.cellLayout.background = AppCompatResources.getDrawable(
            context,
            color
        )
    }
}