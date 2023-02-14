package com.example.projectnailsschedule.presentation.date.dateRecyclerView

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.projectnailsschedule.R
import com.example.projectnailsschedule.presentation.date.DateFragment

class DateViewHolder internal constructor(itemView: View, onItemListener: DateFragment) :
    RecyclerView.ViewHolder(itemView), View.OnClickListener {

    val onItemListener: DateAdapter.OnItemListener
    val appointmentTime: TextView
    val appointmentProcedure: TextView
    val appointmentClientName: TextView
    val appointmentNamePhone: TextView
    val appointmentMisc: TextView

    init {
        appointmentTime = itemView.findViewById(R.id.appointment_time)
        appointmentProcedure = itemView.findViewById(R.id.appointment_procedure)
        appointmentClientName = itemView.findViewById(R.id.appointment_name)
        appointmentNamePhone = itemView.findViewById(R.id.appointmentName_phone)
        appointmentMisc = itemView.findViewById(R.id.appointment_misc)
        this.onItemListener = onItemListener
        itemView.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        TODO("click on appointment (delete/edit)")
    }

}