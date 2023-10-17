package com.example.projectnailsschedule.presentation.search.searchRecyclerVIew

import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.projectnailsschedule.R
import com.example.projectnailsschedule.domain.models.AppointmentModelDb

class SearchViewHolder internal constructor(
    itemView: View,
    listener: SearchRvAdapter.OnItemClickListener
) :
    RecyclerView.ViewHolder(itemView) {

    var appointmentId: Int? = null
    var appointmentDate: String? = null
    var appointmentModelDb: AppointmentModelDb? = null
    var position: Int? = null

    var name: TextView
    var date: TextView
    var phone: TextView
    var time: TextView
    var notes: TextView
    var procedure: TextView

    var vkLink: TextView
    var telegramLink: TextView
    var instagramLink: TextView
    var whatsAppLink: TextView

    var callClientButton: ImageButton
    var vkImageButton: ImageButton
    var telegramImageButton: ImageButton
    var instagramImageButton: ImageButton
    var whatsappImageButton: ImageButton

    var procedureCl: ConstraintLayout
    var phoneCl: ConstraintLayout
    var vkCl: ConstraintLayout
    var telegramCl: ConstraintLayout
    var instagramCl: ConstraintLayout
    var whatsAppCl: ConstraintLayout
    var notesCl: ConstraintLayout

    init {
        with(itemView) {
            name = findViewById(R.id.client_value_search)
            date = findViewById(R.id.date_value_search)
            phone = findViewById(R.id.phone_value_search)
            time = findViewById(R.id.time_value_search)
            notes = findViewById(R.id.client_notes)
            procedure = findViewById(R.id.procedure_value_search)

            vkLink = findViewById(R.id.client_vk_link_tv)
            telegramLink = findViewById(R.id.client_telegram_link_tv)
            instagramLink = findViewById(R.id.client_instagram_link_tv)
            whatsAppLink = findViewById(R.id.client_whatsapp_link_tv)

            callClientButton = findViewById(R.id.call_client_button)
            vkImageButton = findViewById(R.id.vk_logo_imageButton_date)
            telegramImageButton = findViewById(R.id.telegram_logo_imageButton_date)
            instagramImageButton = findViewById(R.id.instagram_logo_imageButton_date)
            whatsappImageButton = findViewById(R.id.whatsapp_logo_imageButton_date)

            procedureCl = findViewById(R.id.client_procedure_cl_date_appointment_rv_item)
            phoneCl = findViewById(R.id.client_phone_cl_date_appointment_rv_item)
            vkCl = findViewById(R.id.client_vk_cl_date_appointment_rv_item)
            telegramCl = findViewById(R.id.client_telegram_cl_date_appointment_rv_item)
            instagramCl = findViewById(R.id.client_instagram_cl_date_appointment_rv_item)
            whatsAppCl = findViewById(R.id.client_whatsapp_cl_date_appointment_rv_item)
            notesCl = findViewById(R.id.client_notes_cl_date_appointment_rv_item)
        }
        itemView.setOnClickListener {
            listener.onItemClick(adapterPosition)
        }
    }
}