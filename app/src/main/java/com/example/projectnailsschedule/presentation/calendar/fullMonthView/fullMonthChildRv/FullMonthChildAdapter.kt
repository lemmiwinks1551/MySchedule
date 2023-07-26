package com.example.projectnailsschedule.presentation.calendar.fullMonthView.fullMonthChildRv

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import androidx.recyclerview.widget.RecyclerView
import com.example.projectnailsschedule.R
import com.example.projectnailsschedule.domain.models.AppointmentModelDb

class FullMonthChildAdapter(
    private val childList: Array<AppointmentModelDb>
) : RecyclerView.Adapter<FullMonthChildViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FullMonthChildViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view: View =
            inflater.inflate(R.layout.appointments_month_view_rv_child, parent, false)
        return FullMonthChildViewHolder(view)
    }

    override fun onBindViewHolder(holder: FullMonthChildViewHolder, position: Int) {
        with(holder) {
            appointmentTime.text = childList[position].time
            appointmentClientName.text = childList[position].name
            appointmentClientPhone.text = childList[position].phone
            appointmentProcedure.text = childList[position].procedure
            appointmentNotes.text = childList[position].notes
        }

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

    override fun getItemCount(): Int {
        return childList.size
    }
}