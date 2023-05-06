package com.example.projectnailsschedule.presentation.date.dateRecyclerView

import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.projectnailsschedule.R
import com.example.projectnailsschedule.domain.models.AppointmentModelDb
import com.example.projectnailsschedule.presentation.date.DateFragment
import java.time.LocalDate

class DateViewHolder internal constructor(itemView: View, onItemListener: DateFragment) :
    RecyclerView.ViewHolder(itemView), View.OnClickListener {

    private val onItemListener: DateAdapter.OnItemListener
    var appointmentId: Int? = null
    var appointmentDate: LocalDate? = null
    var appointmentModelDb: AppointmentModelDb? = null

    val appointmentTime: TextView
    val appointmentProcedure: TextView
    val appointmentClientName: TextView
    val appointmentNamePhone: TextView
    val appointmentMisc: TextView

    var editImageBoolean: ImageButton? = null
    var deleteImageButton: ImageButton? = null

    init {
        appointmentTime = itemView.findViewById(R.id.time_value_search)
        appointmentProcedure = itemView.findViewById(R.id.procedure_value_search)
        appointmentClientName = itemView.findViewById(R.id.client_value_search)
        appointmentNamePhone = itemView.findViewById(R.id.phone_value_search)
        appointmentMisc = itemView.findViewById(R.id.misc_value_search)

        editImageBoolean = itemView.findViewById(R.id.edit_note_image_button)
        deleteImageButton = itemView.findViewById(R.id.delete_forever_image_button)
        this.onItemListener = onItemListener

        itemView.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        onItemListener.onItemClick(adapterPosition)
    }
}