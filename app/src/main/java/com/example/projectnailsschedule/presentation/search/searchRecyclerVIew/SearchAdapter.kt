package com.example.projectnailsschedule.presentation.search.searchRecyclerVIew

import android.app.Dialog
import android.os.Bundle
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
import com.example.projectnailsschedule.domain.models.AppointmentParams
import com.example.projectnailsschedule.presentation.search.SearchFragment
import com.example.projectnailsschedule.presentation.search.SearchViewModel
import java.time.format.DateTimeFormatter

internal class SearchAdapter(
    private var appointmentCount: Int,
    private val searchFragment: SearchFragment,
    private var appointmentsList: List<AppointmentModelDb>,
    private val fragmentActivity: FragmentActivity,
    private val searchViewModel: SearchViewModel
) :
    RecyclerView.Adapter<SearchViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        // Возвращает объект ViewHolder, который будет хранить данные по одному объекту
        val inflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.search_recycler_view_item, parent, false)

        return SearchViewHolder(view, searchFragment)
    }

    private val bindingKeyAppointment = "appointmentParams"

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val appointmentModelDb = appointmentsList[position]

        // declare current day appointments
        val dateAppointment = appointmentsList[position]
        holder.appointmentModelDb = dateAppointment

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
            notes.text = appointmentModelDb.notes
        }

        // set delete image button click listener
        holder.deleteImageButton?.setOnClickListener {
            // run animation
            runAnimation(it)

            val dialog = Dialog(fragmentActivity)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(false)
            dialog.setContentView(R.layout.dialog_ok_cancel)

            // init dialog buttons
            val okButton: Button? = dialog.findViewById(R.id.ok_delete_button)
            val cancelButton: Button? = dialog.findViewById(R.id.cancel_delete_button)

            okButton?.setOnClickListener {
                searchViewModel.deleteAppointment(holder.appointmentModelDb!!)
                notifyItemRemoved(position)
                appointmentCount -= 1
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
                name = holder.name.text.toString(),
                time = holder.time.text.toString(),
                procedure = holder.procedure.text.toString(),
                phone = holder.phone.text.toString(),
                notes = holder.notes.text.toString(),
                deleted = false
            )

            val bundle = Bundle()
            bundle.putParcelable(bindingKeyAppointment, appointmentParams)

            it.findNavController()
                .navigate(R.id.action_nav_search_to_nav_appointment, bundle)
        }
    }

    private fun runAnimation(view: View) {
        // run animation
        view.animate().apply {
            duration = 1000
            rotationY(360f)
        }.start()
    }

    override fun getItemCount(): Int {
        return appointmentCount
    }

    interface OnItemListener {
        fun onItemClick(position: Int, dayText: String?)
    }
}