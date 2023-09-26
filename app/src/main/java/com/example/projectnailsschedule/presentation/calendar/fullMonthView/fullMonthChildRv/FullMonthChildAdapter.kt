package com.example.projectnailsschedule.presentation.calendar.fullMonthView.fullMonthChildRv

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.widget.Toast
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.example.projectnailsschedule.R
import com.example.projectnailsschedule.domain.models.AppointmentModelDb
import com.example.projectnailsschedule.presentation.appointment.selectClient.selectClientRV.SelectClientRVViewHolder

class FullMonthChildAdapter(
    val appointmentsList: MutableList<AppointmentModelDb>,
    private val context: Context,
    private val navController: NavController
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
                val phone = appointmentsList[position].phone.toString()

                if (phone.isEmpty()) {
                    Toast.makeText(context, context.getString(R.string.no_phone_number_error), Toast.LENGTH_LONG).show()
                } else {
                    val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phone"))
                    context.startActivity(intent)
                }
            }
        }

        holder.itemView.setOnClickListener {
            val bundle = Bundle()
            val selectedAppointment = appointmentsList[position]

            bundle.putParcelable(bindingKeyAppointment, selectedAppointment)
            navController.navigate(R.id.action_fullMonthViewFragment_to_nav_appointment, bundle)
        }

        initSocClickListeners(holder)
    }

    private fun initSocClickListeners(holder: FullMonthChildViewHolder) {
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

            whatsAppImageButton.setOnClickListener {
                startWhatsapp(holder.appointmentClientWhatsApp.text.toString().trim())
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