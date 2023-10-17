package com.example.projectnailsschedule.presentation.date.dateRecyclerView

import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.projectnailsschedule.R

class DateViewHolder internal constructor(
    itemView: View,
    listener: DateAdapter.OnItemClickListener
) :
    RecyclerView.ViewHolder(itemView) {

    var appointmentDate: String? = null

    val appointmentTime: TextView
    val appointmentProcedure: TextView
    val appointmentClientName: TextView
    val appointmentClientPhone: TextView
    val appointmentClientVk: TextView
    val appointmentClientTelegram: TextView
    val appointmentClientInstagram: TextView
    val appointmentClientWhatsapp: TextView
    val appointmentNotes: TextView

    var callClientButton: ImageButton
    val vkImageButton: ImageButton
    val telegramImageButton: ImageButton
    val instagramImageButton: ImageButton
    val whatsappImageButton: ImageButton

    var clientTimeCl: ConstraintLayout
    var clientNameCl: ConstraintLayout
    var clientProcedureCl: ConstraintLayout
    var clientPhoneCl: ConstraintLayout
    var clientVkCl: ConstraintLayout
    var clientTelegramCl: ConstraintLayout
    var clientInstagramCl: ConstraintLayout
    var clientWhatsappCl: ConstraintLayout
    var clientNotesCl: ConstraintLayout

    init {
        with(itemView) {
            appointmentTime = findViewById(R.id.time_value_search)
            appointmentProcedure = findViewById(R.id.procedure_value_search)
            appointmentClientName = findViewById(R.id.client_value_search)
            appointmentClientPhone = findViewById(R.id.phone_value_search)
            appointmentClientVk = findViewById(R.id.client_vk_link_tv)
            appointmentClientTelegram = findViewById(R.id.client_telegram_link_tv)
            appointmentClientInstagram = findViewById(R.id.client_instagram_link_tv)
            appointmentClientWhatsapp = findViewById(R.id.client_whatsapp_link_tv)
            appointmentNotes = findViewById(R.id.client_notes)

            callClientButton = findViewById(R.id.call_client_button)
            vkImageButton = findViewById(R.id.vk_logo_imageButton_date)
            telegramImageButton = findViewById(R.id.telegram_logo_imageButton_date)
            instagramImageButton = findViewById(R.id.instagram_logo_imageButton_date)
            whatsappImageButton = findViewById(R.id.whatsapp_logo_imageButton_date)

            clientTimeCl = findViewById(R.id.client_time_cl_date_appointment_rv_item)
            clientNameCl = findViewById(R.id.client_procedure_appointment_month_rv_child)
            clientProcedureCl = findViewById(R.id.client_phone_appointment_month_rv_child)
            clientPhoneCl = findViewById(R.id.client_vk_appointment_month_rv_child)
            clientVkCl = findViewById(R.id.client_telegram_appointment_month_rv_child)
            clientTelegramCl = findViewById(R.id.client_instagram_appointment_month_rv_child)
            clientInstagramCl = findViewById(R.id.client_whatsapp_appointment_month_rv_child)
            clientWhatsappCl = findViewById(R.id.client_notes_appointment_month_rv_child)
            clientNotesCl = findViewById(R.id.client_notes_cl_date_appointment_rv_item)
        }

        itemView.setOnClickListener {
            listener.onItemClick(adapterPosition)
        }
    }
}