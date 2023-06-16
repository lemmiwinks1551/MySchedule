package com.example.projectnailsschedule.presentation.date.dateRecyclerView

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import androidx.fragment.app.FragmentActivity
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.projectnailsschedule.R
import com.example.projectnailsschedule.domain.models.AppointmentModelDb
import com.example.projectnailsschedule.presentation.date.DateFragment
import com.example.projectnailsschedule.presentation.date.DateViewModel

class DateAdapter(
    private var appointmentsCount: Int,
    private val onItemListener: DateFragment,
    private val dateViewModel: DateViewModel,
    private val fragmentActivity: FragmentActivity
) : RecyclerView.Adapter<DateViewHolder>() {

    val log = this::class.simpleName

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DateViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.date_appointments_recycler_view, parent, false)
        return DateViewHolder(view, onItemListener)
    }

    private val bindingKeyAppointment = "appointmentParams"

    override fun onBindViewHolder(holder: DateViewHolder, position: Int) {
        // get date appointments
        val dateAppointmentsArray = dateViewModel.getDateAppointments()

        // declare current day appointments
        val dateAppointment = dateAppointmentsArray[position]

        // set day appointments into recycler view holder
        holder.appointmentModelDb = dateAppointment

        // Set date in holder
        holder.appointmentDate = dateAppointment.date

        // Set time in holder
        holder.appointmentTime.text = dateAppointment.time

        // Set procedure in holder
        holder.appointmentProcedure.text = dateAppointment.procedure

        // Set client name in holder
        holder.appointmentClientName.text = dateAppointment.name

        // Set client phone in holder
        holder.appointmentClientPhone.text = dateAppointment.phone

        // Set notes in holder
        holder.appointmentNotes.text = dateAppointment.notes

        // set delete image button click listener
        holder.deleteImageButton?.setOnClickListener {
            // run animation
            runAnimation(it)

            val dialog = Dialog(fragmentActivity)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(true)
            dialog.setContentView(R.layout.dialog_ok_cancel)
            dialog.window?.setBackgroundDrawableResource(R.color.transparent)

            // init dialog buttons
            val okButton: Button? = dialog.findViewById(R.id.ok_delete_button)
            val cancelButton: Button? = dialog.findViewById(R.id.cancel_delete_button)

            okButton?.setOnClickListener {
                Log.e(log, holder.appointmentModelDb.toString())
                dateViewModel.deleteAppointment(holder.appointmentModelDb!!)
                notifyItemRemoved(position)
                appointmentsCount -= 1
                dialog.dismiss()
            }

            cancelButton?.setOnClickListener {
                dialog.dismiss()
            }

            dialog.show()
        }

        // set edit image button click listener
        holder.editImageBoolean?.setOnClickListener {
            // run animation
            runAnimation(it)

            // get information from clicked item
            // start fragment with selected data

            val appointmentParams = AppointmentModelDb(
                _id = holder.appointmentModelDb!!._id,
                date = holder.appointmentDate,
                name = holder.appointmentClientName.text.toString(),
                time = holder.appointmentTime.text.toString(),
                procedure = holder.appointmentProcedure.text.toString(),
                phone = holder.appointmentClientPhone.text.toString(),
                notes = holder.appointmentNotes.text.toString(),
                deleted = false
            )

            val bundle = Bundle()
            bundle.putParcelable(bindingKeyAppointment, appointmentParams)

            it.findNavController()
                .navigate(R.id.action_dateFragment_to_appointmentFragment, bundle)
        }

        // dateViewModel.setDateAppointments(dateAppointmentsArray)
    }

    private fun runAnimation(view: View) {
        // run animation
        view.animate().apply {
            duration = 1000
            rotationY(360f)
        }.start()
    }

    override fun getItemCount(): Int {
        return appointmentsCount
    }

    interface OnItemListener {
        fun onItemClick(position: Int)
    }
}