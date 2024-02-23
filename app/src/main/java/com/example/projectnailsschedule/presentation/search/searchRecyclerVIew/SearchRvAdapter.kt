package com.example.projectnailsschedule.presentation.search.searchRecyclerVIew

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.projectnailsschedule.R
import com.example.projectnailsschedule.domain.models.AppointmentModelDb
import com.example.projectnailsschedule.presentation.search.SearchViewModel

internal class SearchRvAdapter(
    private var appointmentCount: Int,
    private var appointmentsList: List<AppointmentModelDb>,
    private val searchViewModel: SearchViewModel
) :
    RecyclerView.Adapter<SearchViewHolder>() {
    val log = this::class.simpleName

    private lateinit var mListener: OnItemClickListener

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.search_recycler_view_item, parent, false)

        return SearchViewHolder(view, mListener)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
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
        // set current appointment count
        searchViewModel.appointmentsTotalCount.value = appointmentCount
        searchViewModel.getAllAppointmentsLiveData()
        return appointmentCount
    }

    private fun hideEmptyViews(holder: SearchViewHolder, position: Int) {
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

    private fun clearViews(holder: SearchViewHolder) {
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

    private fun initClickListeners(holder: SearchViewHolder) {
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
        searchViewModel.startVk(uri)
    }

    private fun startTelegram(uri: String) {
        searchViewModel.startTelegram(uri)
    }

    private fun startInstagram(uri: String) {
        searchViewModel.startInstagram(uri)
    }

    private fun startWhatsapp(uri: String) {
        searchViewModel.startWhatsApp(uri)
    }

    private fun startPhone(phoneNum: String) {
        searchViewModel.startPhone(phoneNum)
    }
}