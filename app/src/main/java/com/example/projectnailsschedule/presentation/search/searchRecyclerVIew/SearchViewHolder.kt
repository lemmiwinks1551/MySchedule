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

    var appointmentId: Long? = null
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

            procedureCl = findViewById(R.id.client_phone_appointment_month_rv_child)
            phoneCl = findViewById(R.id.client_vk_appointment_month_rv_child)
            vkCl = findViewById(R.id.client_telegram_appointment_month_rv_child)
            telegramCl = findViewById(R.id.client_instagram_appointment_month_rv_child)
            instagramCl = findViewById(R.id.client_whatsapp_appointment_month_rv_child)
            whatsAppCl = findViewById(R.id.client_notes_appointment_month_rv_child)
            notesCl = findViewById(R.id.client_notes_cl_date_appointment_rv_item)
        }
        itemView.setOnClickListener {
            listener.onItemClick(adapterPosition)
        }
    }
}