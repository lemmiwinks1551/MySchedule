package com.example.projectnailsschedule.presentation.date.dateRecyclerView

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.example.projectnailsschedule.R
import com.example.projectnailsschedule.domain.models.AppointmentModelDb
import com.example.projectnailsschedule.presentation.calendar.DateParamsViewModel

class DateAdapter(
    private var appointmentsList: List<AppointmentModelDb>,
    private val dateParamsViewModel: DateParamsViewModel
) : RecyclerView.Adapter<DateViewHolder>() {

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
            holder.clientPhoto.setImageURI(photo?.toUri())
        }

        hideEmptyViews(holder, position)

        initClickListeners(holder)
    }

    private fun initClickListeners(holder: DateViewHolder) {
        with(holder) {
            callClientButton.setOnClickListener {
                startPhone(holder.appointmentClientPhone.text.toString())
            }

            vkImageButton.setOnClickListener {
                startVk(holder.appointmentClientVk.text.toString().trim())
            }

            telegramImageButton.setOnClickListener {
                startTelegram(holder.appointmentClientTelegram.text.toString().trim())
            }

            instagramImageButton.setOnClickListener {
                startInstagram(holder.appointmentClientInstagram.text.toString().trim())
            }

            whatsappImageButton.setOnClickListener {
                startWhatsapp(holder.appointmentClientWhatsapp.text.toString().trim())
            }
        }
    }

    override fun getItemCount(): Int {
        return appointmentsList.size
    }

    private fun startVk(uri: String) {
        dateParamsViewModel.startVk(uri)
    }

    private fun startTelegram(uri: String) {
        dateParamsViewModel.startTelegram(uri)
    }

    private fun startInstagram(uri: String) {
        dateParamsViewModel.startInstagram(uri)
    }

    private fun startWhatsapp(uri: String) {
        dateParamsViewModel.startWhatsApp(uri)
    }

    private fun startPhone(phoneNum: String) {
        dateParamsViewModel.startPhone(phoneNum)
    }

    private fun hideEmptyViews(holder: DateViewHolder, position: Int) {
        if (appointmentsList[position].time.isNullOrEmpty()) {
            holder.clientTimeCl.visibility = View.GONE
        }
        if (appointmentsList[position].procedure.isNullOrEmpty()) {
            holder.clientProcedureCl.visibility = View.GONE
        }
        if (appointmentsList[position].phone.isNullOrEmpty()) {
            holder.clientPhoneCl.visibility = View.GONE
        }
        if (appointmentsList[position].vk.isNullOrEmpty()) {
            holder.clientVkCl.visibility = View.GONE
        }
        if (appointmentsList[position].telegram.isNullOrEmpty()) {
            holder.clientTelegramCl.visibility = View.GONE
        }
        if (appointmentsList[position].instagram.isNullOrEmpty()) {
            holder.clientInstagramCl.visibility = View.GONE
        }
        if (appointmentsList[position].whatsapp.isNullOrEmpty()) {
            holder.clientWhatsappCl.visibility = View.GONE
        }
        if (appointmentsList[position].notes.isNullOrEmpty()) {
            holder.clientNotesCl.visibility = View.GONE
        }
    }
}