package com.example.projectnailsschedule.presentation.date.dateRecyclerView

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.projectnailsschedule.R
import com.example.projectnailsschedule.domain.models.DateParams
import com.example.projectnailsschedule.presentation.date.DateFragment
import com.example.projectnailsschedule.presentation.date.DateViewModel

class DateAdapter(
    private val appointmentsCount: Int,
    private val onItemListener: DateFragment,
    private val dateViewModel: DateViewModel
) : RecyclerView.Adapter<DateViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DateViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.date_appointments_recycler_view, parent, false)
        return DateViewHolder(view, onItemListener)
    }

    override fun onBindViewHolder(holder: DateViewHolder, position: Int) {
        // get date appointments
        val dateAppointmentsCursor = dateViewModel.getDateAppointments()
        dateAppointmentsCursor.moveToFirst()

        // inflate view holder if data exists
        if (dateAppointmentsCursor.moveToPosition(position)) {
            // Set time in holder
            holder.appointmentTime.text = dateAppointmentsCursor.getString(2)

            // Set procedure in holder
            holder.appointmentProcedure.text = dateAppointmentsCursor.getString(3)

            // Set client name in holder
            holder.appointmentClientName.text = dateAppointmentsCursor.getString(4)

            // Set client phone in holder
            holder.appointmentNamePhone.text = dateAppointmentsCursor.getString(5)

            // Set misc in holder
            holder.appointmentMisc.text = dateAppointmentsCursor.getString(6)
        }
    }

    override fun getItemCount(): Int {
        return appointmentsCount
    }

    interface OnItemListener {
        fun onItemClick(position: Int)
    }

}