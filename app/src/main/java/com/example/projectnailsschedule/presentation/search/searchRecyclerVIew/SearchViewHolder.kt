package com.example.projectnailsschedule.presentation.search.searchRecyclerVIew

import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.projectnailsschedule.R
import com.example.projectnailsschedule.domain.models.AppointmentModelDb
import com.example.projectnailsschedule.presentation.search.SearchFragment
import java.time.LocalDate

class SearchViewHolder internal constructor(itemView: View, onItemListener: SearchFragment) :
    RecyclerView.ViewHolder(itemView), View.OnClickListener {

    private val onItemListener: SearchAdapter.OnItemListener

    var appointmentId: Int? = null
    var appointmentDate: LocalDate? = null
    var appointmentModelDb: AppointmentModelDb? = null

    var name: TextView
    var date: TextView
    var phone: TextView
    var time: TextView
    var misc: TextView
    var procedure: TextView

    var editImageBoolean: ImageButton? = null
    var deleteImageButton: ImageButton? = null

    init {
        name = itemView.findViewById(R.id.client_value_search)
        date = itemView.findViewById(R.id.date_value_search)
        phone = itemView.findViewById(R.id.phone_value_search)
        time = itemView.findViewById(R.id.time_value_search)
        misc = itemView.findViewById(R.id.misc_value_search)
        procedure = itemView.findViewById(R.id.procedure_value_search)

        editImageBoolean = itemView.findViewById(R.id.edit_note_image_button)
        deleteImageButton = itemView.findViewById(R.id.delete_forever_image_button)

        this.onItemListener = onItemListener
        itemView.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        onItemListener.onItemClick(adapterPosition, date.text as String)
    }
}