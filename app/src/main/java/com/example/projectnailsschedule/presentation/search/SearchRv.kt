package com.example.projectnailsschedule.presentation.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.projectnailsschedule.R
import com.example.projectnailsschedule.domain.models.AppointmentModelDb
import com.example.projectnailsschedule.presentation.calendar.DateParamsViewModel

class SearchRv(
    private var appointmentsList: List<AppointmentModelDb>,
    private val dateParamsViewModel: DateParamsViewModel
) : RecyclerView.Adapter<SearchRv.ViewHolder>() {
    inner class ViewHolder(
        itemView: View,
        listener: OnItemClickListener
    ) : RecyclerView.ViewHolder(itemView) {
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

    private lateinit var mListener: OnItemClickListener

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.search_recycler_view_item, parent, false)

        return ViewHolder(view, mListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val appointmentModelDb = appointmentsList[position]

        // declare current day appointments
        val dateAppointment = appointmentsList[position]
        holder.appointmentModelDb = dateAppointment
        holder.position = position

        // get date appointments
        with(holder) {
            this.appointmentModelDb = appointmentModelDb
            appointmentId = appointmentsList[position]._id
            appointmentDate = appointmentModelDb.date
            date.text = appointmentModelDb.date
            time.text = appointmentModelDb.time
            procedure.text = appointmentModelDb.procedure
            name.text = appointmentModelDb.name
            phone.text = appointmentModelDb.phone
            vkLink.text = appointmentModelDb.vk
            telegramLink.text = appointmentModelDb.telegram
            instagramLink.text = appointmentModelDb.instagram
            whatsAppLink.text = appointmentModelDb.whatsapp
            notes.text = appointmentModelDb.notes
        }

        initClickListeners(holder)

        clearViews(holder)

        hideEmptyViews(holder, holder.adapterPosition)
    }

    override fun getItemCount(): Int {
        /*        // set current appointment count
                dateParamsViewModel.appointmentsTotalCount.value = appointmentCount
                dateParamsViewModel.getAllAppointmentsLiveData()*/
        return appointmentsList.size
    }

    private fun hideEmptyViews(holder: ViewHolder, position: Int) {
        with(appointmentsList[position]) {
            if (procedure.isNullOrEmpty()) {
                holder.procedureCl.visibility = View.GONE
            }
            if (phone.isNullOrEmpty()) {
                holder.phoneCl.visibility = View.GONE
            }
            if (vk.isNullOrEmpty()) {
                holder.vkCl.visibility = View.GONE
            }
            if (telegram.isNullOrEmpty()) {
                holder.telegramCl.visibility = View.GONE
            }
            if (instagram.isNullOrEmpty()) {
                holder.instagramCl.visibility = View.GONE
            }
            if (whatsapp.isNullOrEmpty()) {
                holder.whatsAppCl.visibility = View.GONE
            }
            if (notes.isNullOrEmpty()) {
                holder.notesCl.visibility = View.GONE
            }
        }
    }

    private fun clearViews(holder: ViewHolder) {
        with(holder) {
            procedureCl.visibility = View.VISIBLE
            phoneCl.visibility = View.VISIBLE
            vkCl.visibility = View.VISIBLE
            telegramCl.visibility = View.VISIBLE
            instagramCl.visibility = View.VISIBLE
            whatsAppCl.visibility = View.VISIBLE
            notesCl.visibility = View.VISIBLE
        }
    }

    private fun initClickListeners(holder: ViewHolder) {
        with(holder) {
            vkImageButton.setOnClickListener {
                startVk(holder.vkLink.text.toString().trim())
            }

            telegramImageButton.setOnClickListener {
                startTelegram(holder.telegramLink.text.toString().trim())
            }

            instagramImageButton.setOnClickListener {
                startInstagram(holder.instagramLink.text.toString().trim())
            }

            whatsappImageButton.setOnClickListener {
                startWhatsapp(holder.whatsAppLink.text.toString().trim())
            }

            callClientButton.setOnClickListener {
                startPhone(holder.phone.text.toString())
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