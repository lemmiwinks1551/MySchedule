package com.example.projectnailsschedule.presentation.calendar.listMonthView.fullMonthChildRv

import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.projectnailsschedule.R
import com.example.projectnailsschedule.domain.models.AppointmentModelDb

class FullMonthChildViewHolder internal constructor(
    itemView: View
) : RecyclerView.ViewHolder(itemView) {

    var appointmentModelDb: AppointmentModelDb? = null

    val cl3: ConstraintLayout
    val cl4: ConstraintLayout
    val cl5: ConstraintLayout
    val cl6: ConstraintLayout
    val cl7: ConstraintLayout
    val cl8: ConstraintLayout

    val appointmentTime: TextView
    val appointmentProcedure: TextView
    val appointmentClientName: TextView
    val appointmentClientPhone: TextView
    val appointmentClientVk: TextView
    val appointmentClientTelegram: TextView
    val appointmentClientInstagram: TextView
    val appointmentClientWhatsApp: TextView
    val appointmentNotes: TextView

    val phoneCallButton: ImageView
    val vkImageButton: ImageView
    val telegramImageButton: ImageView
    val instagramImageButton: ImageView
    val whatsAppImageButton: ImageView
    val expandButton: ImageView
    val collapseButton: ImageView
    var clientPhoto: ImageView

    init {
        with(itemView) {
            cl3 = findViewById(R.id.client_phone_appointment_month_rv_child)
            cl4 = findViewById(R.id.client_vk_appointment_month_rv_child)
            cl5 = findViewById(R.id.client_telegram_appointment_month_rv_child)
            cl6 = findViewById(R.id.client_instagram_appointment_month_rv_child)
            cl7 = findViewById(R.id.client_whatsapp_appointment_month_rv_child)
            cl8 = findViewById(R.id.client_notes_appointment_month_rv_child)

            appointmentTime = findViewById(R.id.child_time_value_child)
            appointmentProcedure = findViewById(R.id.procedure_value_child)
            appointmentClientName = findViewById(R.id.child_client_value_child)
            appointmentClientPhone = findViewById(R.id.phone_value_child)
            appointmentClientVk = findViewById(R.id.client_vk_link_tv_child)
            appointmentClientTelegram = findViewById(R.id.client_telegram_link_tv_child)
            appointmentClientInstagram = findViewById(R.id.client_instagram_link_tv_child)
            appointmentClientWhatsApp = findViewById(R.id.client_whatsapp_link_tv_child)
            appointmentNotes = findViewById(R.id.notes_value_child)
            clientPhoto = findViewById(R.id.client_avatar_date_appointment)

            phoneCallButton = findViewById(R.id.phone_call_button)
            vkImageButton = findViewById(R.id.vk_logo_imageButton_child)
            telegramImageButton = findViewById(R.id.telegram_logo_imageButton_child)
            instagramImageButton = findViewById(R.id.instagram_logo_imageButton_child)
            whatsAppImageButton = findViewById(R.id.whatsapp_logo_imageButton_child)
            expandButton = findViewById(R.id.expand_button)
            collapseButton = findViewById(R.id.collapse_button)
        }
    }
}