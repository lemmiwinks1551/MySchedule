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
    var clientProcedureCl: ConstraintLayout
    var clientPhoneCl: ConstraintLayout
    var clientVkCl: ConstraintLayout
    var clientTelegramCl: ConstraintLayout
    var clientInstagramCl: ConstraintLayout
    var clientWhatsappCl: ConstraintLayout
    var clientNotesCl: ConstraintLayout

    init {
        with(itemView) {
            appointmentTime = findViewById(R.id.appointment_time_tv)
            appointmentProcedure = findViewById(R.id.procedure_tv_date_appointment)
            appointmentClientName = findViewById(R.id.client_name_date_appointment)
            appointmentClientPhone = findViewById(R.id.phone_date_appointment)
            appointmentClientVk = findViewById(R.id.vk_date_appointment)
            appointmentClientTelegram = findViewById(R.id.telegram_date_appointment)
            appointmentClientInstagram = findViewById(R.id.instagram_date_appointment)
            appointmentClientWhatsapp = findViewById(R.id.whatsapp_date_appointment)
            appointmentNotes = findViewById(R.id.notes_date_appointment)

            callClientButton = findViewById(R.id.call_client_button_date_appointment)
            vkImageButton = findViewById(R.id.vk_logo_imageButton_date_appointment)
            telegramImageButton = findViewById(R.id.telegram_logo_imageButton_date_appointment)
            instagramImageButton = findViewById(R.id.instagram_logo_imageButton_date_appointment)
            whatsappImageButton = findViewById(R.id.whatsapp_logo_imageButton_date_appointment)

            clientTimeCl = findViewById(R.id.appointment_time_cl)
            clientProcedureCl = findViewById(R.id.appointment_procedure_cl)
            clientPhoneCl = findViewById(R.id.client_phone_cl)
            clientVkCl = findViewById(R.id.client_vk_cl)
            clientTelegramCl = findViewById(R.id.client_telegram_cl)
            clientInstagramCl = findViewById(R.id.client_instagram_cl)
            clientWhatsappCl = findViewById(R.id.client_whatsapp_cl)
            clientNotesCl = findViewById(R.id.client_notes_cl)
        }

        itemView.setOnClickListener {
            listener.onItemClick(adapterPosition)
        }
    }
}