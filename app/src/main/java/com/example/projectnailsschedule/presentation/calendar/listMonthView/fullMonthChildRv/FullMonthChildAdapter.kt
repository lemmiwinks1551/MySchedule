package com.example.projectnailsschedule.presentation.calendar.listMonthView.fullMonthChildRv

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.example.projectnailsschedule.R
import com.example.projectnailsschedule.domain.models.AppointmentModelDb
import com.example.projectnailsschedule.presentation.calendar.DateParamsViewModel

class FullMonthChildAdapter(
    val appointmentsList: MutableList<AppointmentModelDb>,
    private val navController: NavController,
    private val dateParamsViewModel: DateParamsViewModel
) : RecyclerView.Adapter<FullMonthChildViewHolder>() {

    private val bindingKeyAppointment = "appointmentParams"

    override fun getItemCount(): Int {
        return appointmentsList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FullMonthChildViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view: View =
            inflater.inflate(R.layout.appointments_month_view_rv_child, parent, false)

        return FullMonthChildViewHolder(view)
    }

    override fun onBindViewHolder(holder: FullMonthChildViewHolder, position: Int) {
        holder.appointmentModelDb = appointmentsList[position]
        setOnClickListeners(holder, position)
        fillTextViews(holder)
    }

    private fun fillTextViews(holder: FullMonthChildViewHolder) {
        with(appointmentsList[holder.adapterPosition]) {
            holder.appointmentTime.text = time
            holder.appointmentClientName.text = name
            holder.appointmentClientPhone.text = phone
            holder.appointmentClientVk.text = vk
            holder.appointmentClientTelegram.text = telegram
            holder.appointmentClientInstagram.text = instagram
            holder.appointmentClientWhatsApp.text = whatsapp
            holder.appointmentProcedure.text = procedure
            holder.appointmentNotes.text = notes
        }
    }

    private fun expandFields(holder: FullMonthChildViewHolder) {
        with(holder) {
            cl3.visibility = View.VISIBLE
            cl4.visibility = View.VISIBLE
            cl5.visibility = View.VISIBLE
            cl6.visibility = View.VISIBLE
            cl7.visibility = View.VISIBLE
            cl8.visibility = View.VISIBLE
        }
    }

    private fun collapseFields(holder: FullMonthChildViewHolder) {
        with(holder) {
            cl3.visibility = View.GONE
            cl4.visibility = View.GONE
            cl5.visibility = View.GONE
            cl6.visibility = View.GONE
            cl7.visibility = View.GONE
            cl8.visibility = View.GONE
        }
    }

    private fun setOnClickListeners(holder: FullMonthChildViewHolder, position: Int) {
        with(holder) {
            expandButton.setOnClickListener {
                expandButton.visibility = View.INVISIBLE
                collapseButton.visibility = View.VISIBLE

                expandFields(holder = holder)
            }

            collapseButton.setOnClickListener {
                collapseFields(holder = holder)

                expandButton.visibility = View.VISIBLE
                collapseButton.visibility = View.INVISIBLE
            }

            phoneCallButton.setOnClickListener {
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

            whatsAppImageButton.setOnClickListener {
                startWhatsapp(holder.appointmentClientWhatsApp.text.toString().trim())
            }

            itemView.setOnClickListener {
                val bundle = Bundle()
                val selectedAppointment = appointmentsList[position]

                bundle.putParcelable(bindingKeyAppointment, selectedAppointment)
                navController.navigate(R.id.action_fullMonthViewFragment_to_nav_appointment, bundle)
            }
        }
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
}