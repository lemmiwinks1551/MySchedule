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
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.projectnailsschedule.R
import com.example.projectnailsschedule.domain.models.DateParams
import com.example.projectnailsschedule.presentation.calendar.CalendarViewModel
import com.example.projectnailsschedule.util.Util
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

            // If appointments exists
            if (appointmentCount > 0) {
                holder.dateAppointmentsCount.text = appointmentCount.toString()
            }

            // If the day corresponds to today's date, set the text color to red
            if (dateParams.date!! == LocalDate.now()) {
                holder.date.setTextColor(Color.RED)
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
                setDateColorDialog(holder, dateParams.date!!)
                true
            }
        }
    }

    override fun getItemCount(): Int {
        return daysInMonth.size
    }

    private fun setDateColorDialog(holder: ViewHolder, date: LocalDate) {
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
            context.getString(R.string.formatted_date, Util().formatDate(date))

        val dialog = builder.create()
        val layoutParams = WindowManager.LayoutParams()

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialog.window?.attributes = layoutParams

        dialog.show()
    }
}