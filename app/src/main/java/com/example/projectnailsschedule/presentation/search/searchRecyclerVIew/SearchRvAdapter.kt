package com.example.projectnailsschedule.presentation.search.searchRecyclerVIew

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.projectnailsschedule.R
import com.example.projectnailsschedule.domain.models.AppointmentModelDb
import com.example.projectnailsschedule.presentation.date.dateRecyclerView.DateViewHolder
import com.example.projectnailsschedule.presentation.search.SearchViewModel

internal class SearchRvAdapter(
    private var appointmentCount: Int,
    private var appointmentsList: List<AppointmentModelDb>,
    private val searchViewModel: SearchViewModel,
    private val context: Context
) :
    RecyclerView.Adapter<SearchViewHolder>()
{
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

        holder.callClientButton.setOnClickListener {
            val phone = appointmentsList[position].phone.toString()
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phone"))
            context.startActivity(intent)
        }

        initSocClickListeners(holder)
    }

    override fun getItemCount(): Int {
        // set current appointment count
        searchViewModel.appointmentsTotalCount.value = appointmentCount
        searchViewModel.getAllAppointmentsLiveData()
        return appointmentCount
    }

    private fun initSocClickListeners(holder: SearchViewHolder) {
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
        }
    }

    private fun startVk(uri: String) {
        val uri = uri.replace("https://vk.com/", "")
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://vk.com/$uri"))
            context.startActivity(intent)
        } catch (e: Exception) {
            try {
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://vk.com/$uri"))
                context.startActivity(browserIntent)
            } catch (e: Exception) {
                Toast.makeText(
                    context,
                    context.getString(R.string.unknown_error),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun startTelegram(uri: String) {
        val uri = uri.replace("https://t.me/", "")
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("tg://resolve?domain=$uri"))
            context.startActivity(intent)
        } catch (e: Exception) {
            try {
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/$uri"))
                context.startActivity(browserIntent)
            } catch (e: Exception) {
                Toast.makeText(
                    context,
                    context.getString(R.string.unknown_error),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun startInstagram(uri: String) {
        val regex = Regex("https:\\/\\/instagram\\.com\\/([^?\\/]+)(?:\\?igshid=.*)?|([\\w.-]+)")
        val matchResult = regex.find(uri)
        var username = matchResult?.groupValues?.getOrNull(1)
        if (username == "") {
            username = matchResult?.groupValues?.getOrNull(2)
        }

        try {
            val intent =
                Intent(Intent.ACTION_VIEW, Uri.parse("http://instagram.com/_u/$username"))
            intent.setPackage("com.instagram.android")
            context.startActivity(intent)
        } catch (e: Exception) {
            try {
                val browserIntent =
                    Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/$username"))
                context.startActivity(browserIntent)
            } catch (e: Exception) {
                Toast.makeText(
                    context,
                    context.getString(R.string.unknown_error),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun startWhatsapp(uri: String) {
        try {
            val intent =
                Intent(Intent.ACTION_VIEW, Uri.parse("https://api.whatsapp.com/send?phone=$uri"))
            context.startActivity(intent)
        } catch (e: Exception) {
            try {
                val browserIntent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://web.whatsapp.com/send?phone=$uri")
                )
                context.startActivity(browserIntent)
            } catch (e: Exception) {
                Toast.makeText(
                    context,
                    context.getString(R.string.unknown_error),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}