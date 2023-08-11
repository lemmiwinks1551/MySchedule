package com.example.projectnailsschedule.presentation.date.dateRecyclerView

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.projectnailsschedule.R
import com.example.projectnailsschedule.domain.models.AppointmentModelDb
import java.lang.String

class DateAdapter(
    private var appointmentsCount: Int,
    private var appointmentsList: List<AppointmentModelDb>,
    private val context: Context
) : RecyclerView.Adapter<DateViewHolder>() {

    val log = this::class.simpleName

    private lateinit var mListener: OnItemClickListener

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DateViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.date_appointments_rv_item, parent, false)
        return DateViewHolder(view, mListener)
    }

    override fun onBindViewHolder(holder: DateViewHolder, position: Int) {
        with(appointmentsList[position]) {
            holder.appointmentDate = date
            holder.appointmentTime.text = time
            holder.appointmentProcedure.text = procedure
            holder.appointmentClientName.text = name
            holder.appointmentClientPhone.text = phone
            holder.appointmentClientVk.text = vk
            holder.appointmentClientTelegram.text = telegram
            holder.appointmentClientInstagram.text = instagram
            holder.appointmentClientWhatsapp.text = whatsapp
            holder.appointmentNotes.text = notes
        }


        // set callClient click listener
        holder.callClientButton.setOnClickListener {
            val phone = appointmentsList[position].phone
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phone"))
            context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {
        return appointmentsCount
    }
}