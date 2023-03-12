package com.example.projectnailsschedule.presentation.date.dateRecyclerView

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.projectnailsschedule.R
import com.example.projectnailsschedule.domain.models.AppointmentParams
import com.example.projectnailsschedule.presentation.date.DateFragment
import com.example.projectnailsschedule.presentation.date.DateViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class DateAdapter(
    private var appointmentsCount: Int,
    private val onItemListener: DateFragment,
    private val dateViewModel: DateViewModel,
    private val fragmentActivity: FragmentActivity
) : RecyclerView.Adapter<DateViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DateViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.date_appointments_recycler_view, parent, false)
        return DateViewHolder(view, onItemListener)
    }

    private val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
    private val bindingKeyAppointment = "appointmentParams"

    override fun onBindViewHolder(holder: DateViewHolder, position: Int) {
        // get date appointments
        val dateAppointmentsCursor = dateViewModel.getDateAppointments()

        // inflate view holder if data exists
        if (dateAppointmentsCursor.moveToPosition(position)) {
            // Set id in holder
            holder.appointmentId = dateAppointmentsCursor.getInt(0)

            // Set date in holder
            holder.appointmentDate = LocalDate.parse(dateAppointmentsCursor.getString(1))

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

        // set delete image button click listener
        holder.deleteImageButton?.setOnClickListener{
            dateViewModel.deleteAppointment(position)
            notifyItemRemoved(position)
            appointmentsCount -= 1
        }

        holder.editImageBoolean?.setOnClickListener {

            // get information from clicked item
            // start fragment with selected data

            val appointmentParams = AppointmentParams(
                _id = holder.appointmentId,
                appointmentDate = holder.appointmentDate,
                clientName = holder.appointmentClientName.text.toString(),
                startTime = holder.appointmentTime.text.toString(),
                procedure = holder.appointmentProcedure.text.toString(),
                phoneNum = holder.appointmentNamePhone.text.toString(),
                misc = holder.appointmentMisc.text.toString()
            )

            val bundle = Bundle()
            bundle.putParcelable(bindingKeyAppointment, appointmentParams)

            fragmentActivity.findNavController(R.id.addButton)
                .navigate(R.id.action_dateFragment_to_appointmentFragment, bundle)
        }

        dateViewModel.setDateAppointments(dateAppointmentsCursor)
    }

    override fun getItemCount(): Int {
        return appointmentsCount
    }

    interface OnItemListener {
        fun onItemClick(position: Int)
    }

}