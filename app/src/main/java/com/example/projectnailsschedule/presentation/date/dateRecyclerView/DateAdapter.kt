package com.example.projectnailsschedule.presentation.date.dateRecyclerView

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
import com.example.projectnailsschedule.presentation.clients.clientsRecyclerView.ClientsViewHolder
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

        initSocClickListeners(holder)

    }

    override fun getItemCount(): Int {
        return appointmentsCount
    }

    private fun initSocClickListeners(holder: DateViewHolder) {
        with(holder) {
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

    private fun startVk(uri: kotlin.String) {
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
                    "Не удалось перейти во Вконтакте",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun startTelegram(uri: kotlin.String) {
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
                    "Не удалось перейти в Telegram",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun startInstagram(uri: kotlin.String) {
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
                    "Не удалось перейти в Instagram",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun startWhatsapp(uri: kotlin.String) {
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
                    "Не удалось перейти в Whatsapp",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}