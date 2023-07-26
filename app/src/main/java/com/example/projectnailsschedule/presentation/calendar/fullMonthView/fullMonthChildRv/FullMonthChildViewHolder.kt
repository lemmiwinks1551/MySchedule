package com.example.projectnailsschedule.presentation.calendar.fullMonthView.fullMonthChildRv

import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.projectnailsschedule.R

class FullMonthChildViewHolder internal constructor(
    itemView: View
) : RecyclerView.ViewHolder(itemView) {

    val appointmentTime: TextView
    val appointmentProcedure: TextView
    val appointmentClientName: TextView
    val appointmentClientPhone: TextView
    val appointmentNotes: TextView

    val phoneCallButton: ImageButton
    val expandButton: ImageButton
    val collapseButton: ImageButton

    init {
        appointmentTime = itemView.findViewById(R.id.child_time_value)
        appointmentProcedure = itemView.findViewById(R.id.child_procedure_value)
        appointmentClientName = itemView.findViewById(R.id.child_client_value)
        appointmentClientPhone = itemView.findViewById(R.id.child_phone_value)
        appointmentNotes = itemView.findViewById(R.id.child_misc_value)

        phoneCallButton = itemView.findViewById(R.id.phone_call_button)

        expandButton = itemView.findViewById(R.id.expand_button)
        collapseButton = itemView.findViewById(R.id.collapse_button)
    }
}