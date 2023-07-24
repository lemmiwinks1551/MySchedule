package com.example.projectnailsschedule.presentation.calendar.fullMonthView.fullMonthViewRV

import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.projectnailsschedule.R

class FullMonthViewRVViewHolder internal constructor(
    itemView: View,
    listener: FullMonthViewRVAdapter.OnItemClickListener
) : RecyclerView.ViewHolder(itemView) {

    val appointmentConstraintLayout: ConstraintLayout
    val appointmentDate: TextView
    val appointmentTime: TextView
    val appointmentProcedure: TextView
    val appointmentClientName: TextView
    val appointmentClientPhone: TextView
    val appointmentNotes: TextView
    val noDataTextView: TextView

    var callClientButton: ImageButton

    init {
        appointmentConstraintLayout = itemView.findViewById(R.id.appointment_constraint_layout)
        appointmentDate = itemView.findViewById(R.id.date_value_search)
        appointmentTime = itemView.findViewById(R.id.time_value_search)
        appointmentProcedure = itemView.findViewById(R.id.procedure_value_search)
        appointmentClientName = itemView.findViewById(R.id.client_value_search)
        appointmentClientPhone = itemView.findViewById(R.id.phone_value_search)
        appointmentNotes = itemView.findViewById(R.id.misc_value_search)
        noDataTextView = itemView.findViewById(R.id.no_data_text_view)

        callClientButton = itemView.findViewById(R.id.call_client_button)

        itemView.setOnClickListener {
            listener.onItemClick(adapterPosition)
        }
    }
}