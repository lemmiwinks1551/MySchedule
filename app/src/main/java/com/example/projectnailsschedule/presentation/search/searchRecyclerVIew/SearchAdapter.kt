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
import com.example.projectnailsschedule.domain.models.AppointmentParams
import com.example.projectnailsschedule.presentation.search.SearchFragment
import com.example.projectnailsschedule.presentation.search.SearchViewModel

internal class SearchAdapter(
    private var appointmentCount: Int,
    private val searchFragment: SearchFragment,
    private var appointmentsList: MutableList<AppointmentParams>,
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

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        // get date appointments
        val allAppointmentsCursor = searchViewModel.allAppointmentsCursor

        holder.date.text = appointmentsList[position].appointmentDate.toString()
        holder.time.text = appointmentsList[position].startTime.toString()
        holder.procedure.text = appointmentsList[position].procedure.toString()
        holder.name.text = appointmentsList[position].clientName.toString()
        holder.phone.text = appointmentsList[position].phoneNum.toString()
        holder.misc.text = appointmentsList[position].misc.toString()

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
                searchViewModel.deleteAppointment(position)
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
        /*holder.editImageBoolean?.setOnClickListener {
            // run animation
            runAnimation(it)

            // get information from clicked item
            // start fragment with selected data

            val appointmentParams = AppointmentParams(
                _id = holder.appointmentId,
                appointmentDate = holder.appointmentDate,
                clientName = holder.appointmentClientName.text.toString(),
                startTime = holder.appointmentTime.text.toString(),
                procedure = holder.appointmentProcedure.text.toString(),
                phoneNum = holder.appointmentNamePhone.text.toString(),
                misc = holder.appointmentMisc.text.toString(),
                deleted = 0
            )

            val bundle = Bundle()
            bundle.putParcelable(bindingKeyAppointment, appointmentParams)

            fragmentActivity.findNavController(R.id.addButton)
                .navigate(R.id.action_dateFragment_to_appointmentFragment, bundle)
        }*/


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