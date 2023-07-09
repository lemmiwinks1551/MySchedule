package com.example.projectnailsschedule.presentation.date.dateRecyclerView

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.projectnailsschedule.R
import com.example.projectnailsschedule.domain.models.AppointmentModelDb

class DateAdapter(
    private var appointmentsCount: Int,
    private var appointmentsList: List<AppointmentModelDb>,
    private val context: Context
) : RecyclerView.Adapter<DateViewHolder>() {

    val log = this::class.simpleName

    private lateinit var mListener: OnItemClickListener

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DateViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.date_appointments_recycler_view, parent, false)
        return DateViewHolder(view, mListener)
    }

    override fun onBindViewHolder(holder: DateViewHolder, position: Int) {
        with(holder) {
            // set date in holder
            appointmentDate = appointmentsList[position].date

            // set time in holder
            appointmentTime.text = appointmentsList[position].time

            // set procedure in holder
            appointmentProcedure.text = appointmentsList[position].procedure

            // set client name in holder
            appointmentClientName.text = appointmentsList[position].name

            // set client phone in holder
            appointmentClientPhone.text = appointmentsList[position].phone

            // set notes in holder
            appointmentNotes.text = appointmentsList[position].notes

            // set callClient click listener
            holder.callClientButton.setOnClickListener {
                val phone = appointmentsList[position].phone
                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phone"))
                context.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int {
        return appointmentsCount
    }
}