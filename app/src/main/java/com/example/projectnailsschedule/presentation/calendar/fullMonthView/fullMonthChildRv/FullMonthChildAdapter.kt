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
        with(holder) {
            appointmentTime.text = appointmentsList[position].time
            appointmentClientName.text = appointmentsList[position].name
            appointmentClientPhone.text = appointmentsList[position].phone
            appointmentProcedure.text = appointmentsList[position].procedure
            appointmentNotes.text = appointmentsList[position].notes
        }
    }

    private fun expandFields(holder: FullMonthChildViewHolder) {
        val fadeInAnimation = AlphaAnimation(0.0f, 1.0f)
        fadeInAnimation.duration = 1000

        with(holder) {
            appointmentProcedure.visibility = View.VISIBLE
            appointmentProcedure.startAnimation(fadeInAnimation)

            appointmentClientPhone.visibility = View.VISIBLE
            appointmentClientPhone.startAnimation(fadeInAnimation)

            appointmentNotes.visibility = View.VISIBLE
            appointmentNotes.startAnimation(fadeInAnimation)

            phoneCallButton.visibility= View.VISIBLE
            phoneCallButton.startAnimation(fadeInAnimation)
        }
    }

    private fun collapseFields(holder: FullMonthChildViewHolder) {
        with(holder) {
            appointmentProcedure.visibility = View.GONE
            appointmentClientPhone.visibility = View.GONE
            appointmentNotes.visibility = View.GONE
            phoneCallButton.visibility= View.GONE
        }
    }

    private fun setOnClickListeners(holder: FullMonthChildViewHolder, position: Int) {
        with(holder) {
            expandButton.setOnClickListener {
                expandButton.visibility = View.GONE
                collapseButton.visibility = View.VISIBLE

                expandFields(holder = holder)
            }

            collapseButton.setOnClickListener {
                collapseFields(holder = holder)

                expandButton.visibility = View.VISIBLE
                collapseButton.visibility = View.GONE
            }

            phoneCallButton.setOnClickListener {
                val phone = appointmentsList[position].phone.toString()

                if (phone.isEmpty()) {
                    Toast.makeText(context, "Номер телефона пуст", Toast.LENGTH_LONG).show()
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
    }
}