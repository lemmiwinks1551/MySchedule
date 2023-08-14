package com.example.projectnailsschedule.presentation.calendar.fullMonthView.fullMonthChildRv

import android.view.View
import android.widget.ImageButton
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

    val phoneCallButton: ImageButton
    val vkButton: ImageButton
    val telegramButton: ImageButton
    val instagramButton: ImageButton
    val whatsAppButton: ImageButton
    val expandButton: ImageButton
    val collapseButton: ImageButton

    init {
        cl3 = itemView.findViewById(R.id.constraintLayout3)
        cl4 = itemView.findViewById(R.id.constraintLayout4)
        cl5 = itemView.findViewById(R.id.constraintLayout5)
        cl6 = itemView.findViewById(R.id.constraintLayout6)
        cl7 = itemView.findViewById(R.id.constraintLayout7)
        cl8 = itemView.findViewById(R.id.constraintLayout8)

        appointmentTime = itemView.findViewById(R.id.child_time_value_child)
        appointmentProcedure = itemView.findViewById(R.id.procedure_value_child)
        appointmentClientName = itemView.findViewById(R.id.child_client_value_child)
        appointmentClientPhone = itemView.findViewById(R.id.phone_value_child)
        appointmentClientVk = itemView.findViewById(R.id.client_vk_link_tv_child)
        appointmentClientTelegram = itemView.findViewById(R.id.client_telegram_link_tv_child)
        appointmentClientInstagram = itemView.findViewById(R.id.client_instagram_link_tv_child)
        appointmentClientWhatsApp = itemView.findViewById(R.id.client_whatsapp_link_tv_child)
        appointmentNotes = itemView.findViewById(R.id.notes_value_child)

        phoneCallButton = itemView.findViewById(R.id.phone_call_button)
        vkButton = itemView.findViewById(R.id.vk_logo_imageButton_child)
        telegramButton = itemView.findViewById(R.id.telegram_logo_imageButton_child)
        instagramButton = itemView.findViewById(R.id.instagram_logo_imageButton_child)
        whatsAppButton = itemView.findViewById(R.id.whatsapp_logo_imageButton_child)
        expandButton = itemView.findViewById(R.id.expand_button)
        collapseButton = itemView.findViewById(R.id.collapse_button)
    }
}