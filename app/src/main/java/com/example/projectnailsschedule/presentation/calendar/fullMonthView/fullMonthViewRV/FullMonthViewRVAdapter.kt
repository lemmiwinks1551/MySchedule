package com.example.projectnailsschedule.presentation.calendar.fullMonthView.fullMonthViewRV

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.projectnailsschedule.R
import com.example.projectnailsschedule.domain.models.AppointmentModelDb
import com.example.projectnailsschedule.domain.models.DateWeekAppModel
import com.example.projectnailsschedule.presentation.calendar.fullMonthView.FullMonthViewViewModel
import com.example.projectnailsschedule.presentation.calendar.fullMonthView.fullMonthChildRv.FullMonthChildAdapter
import com.example.projectnailsschedule.presentation.calendar.fullMonthView.fullMonthChildRv.FullMonthChildViewHolder
import com.example.projectnailsschedule.util.Util
import com.google.android.material.snackbar.Snackbar
import java.time.LocalDate

class FullMonthViewRVAdapter(
    private val monthDatesList: MutableList<DateWeekAppModel>,
    private val context: Context,
    private val navController: NavController,
    private val fullMonthViewViewModel: FullMonthViewViewModel
) : RecyclerView.Adapter<FullMonthViewRVViewHolder>(
) {
    lateinit var fullMonthChildAdapter: FullMonthChildAdapter
    lateinit var view: View
    private val bindingKeyAppointment = "appointmentParams"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FullMonthViewRVViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        view =
            inflater.inflate(R.layout.appointments_month_view_rv_parent_item, parent, false)
        return FullMonthViewRVViewHolder(view)
    }

    override fun getItemCount(): Int {
        return monthDatesList.size
    }

    override fun onBindViewHolder(holder: FullMonthViewRVViewHolder, position: Int) {
        // set date and week day
        fillDateTv(holder)

        // init clickListener
        initClickListener(holder)

        if (monthDatesList[position].appointmentsList.isEmpty()) {
            // if no appointments in current date
            fillNoAppointmentsTv(holder)
        } else {
            // if appointments exists in current date
            fillAppointmentsExistsTv(holder)
            inflateChildRv(holder, position)
        }
    }

    private fun fillDateTv(holder: FullMonthViewRVViewHolder) {
        // set values into date and weekName text views
        with(holder) {
            holder.date.text = monthDatesList[position].date.dayOfMonth.toString()
            holder.weekDayName.text = monthDatesList[position].weekDay

            // if day is today set custom frame
            if (monthDatesList[position].date == LocalDate.now()) {
                holder.dateCl.setBackgroundResource(R.drawable.calendar_recycler_view_borders_today)
            } else {
                holder.dateCl.setBackgroundColor(context.resources.getColor(R.color.transparent))
            }
        }
    }

    private fun fillNoAppointmentsTv(holder: FullMonthViewRVViewHolder) {
        // fill views if no appointments
        with(holder) {
            childRv.visibility = View.GONE
        }
    }

    private fun fillAppointmentsExistsTv(holder: FullMonthViewRVViewHolder) {
        // fill text views if appointments exists
        with(holder) {
            childRv.visibility = View.VISIBLE
        }
    }

    private fun inflateChildRv(holder: FullMonthViewRVViewHolder, position: Int) {
        val parentItem = monthDatesList[position]

        with(holder) {
            childRv.setHasFixedSize(true)
            childRv.layoutManager = GridLayoutManager(holder.itemView.context, 1)

            fullMonthChildAdapter =
                FullMonthChildAdapter(
                    parentItem.appointmentsList,
                    context,
                    navController,
                    fullMonthViewViewModel
                )
            childRv.adapter = fullMonthChildAdapter
        }

        swipeToDelete(holder)
    }

    private fun swipeToDelete(holder: FullMonthViewRVViewHolder) {
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                // this method is called when the item is moved.
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                val childPosition = viewHolder.adapterPosition
                val parentPosition = holder.adapterPosition

                val deleteAppointmentModelDb: AppointmentModelDb =
                    (viewHolder as FullMonthChildViewHolder).appointmentModelDb!!

                // save position to restore if need
                val posToRestore = (holder.childRv.adapter as FullMonthChildAdapter)

                // delete client from Db
                fullMonthViewViewModel.deleteAppointment(deleteAppointmentModelDb)

                // delete in child rv
                (holder.childRv.adapter as FullMonthChildAdapter).appointmentsList.remove(
                    deleteAppointmentModelDb
                )

                // update child rv
                fullMonthChildAdapter.notifyItemRemoved(childPosition)

                // update parent rv
                this@FullMonthViewRVAdapter.notifyItemRangeChanged(
                    parentPosition,
                    this@FullMonthViewRVAdapter.itemCount - parentPosition
                )

                // show Snackbar
                Snackbar.make(
                    holder.childRv,
                    context.getString(
                        R.string.deleted_appointment_text,
                        deleteAppointmentModelDb.name
                    ),
                    Snackbar.LENGTH_LONG
                ).setBackgroundTint(Color.parseColor("#ffff00"))
                    .setActionTextColor(Color.parseColor("#003300"))
                    .setTextColor(Color.parseColor("#003300"))
                    .setAction(
                        context.getString(R.string.cancel)
                    ) {
                        // restore appointment in Db
                        fullMonthViewViewModel.saveAppointment(deleteAppointmentModelDb)

                        // restore appointment in child list
                        posToRestore.appointmentsList.add(
                            childPosition,
                            deleteAppointmentModelDb
                        )

                        // update child rv
                        fullMonthChildAdapter.notifyItemChanged(childPosition)

                        // update parent rv
                        this@FullMonthViewRVAdapter.notifyItemRangeChanged(
                            parentPosition,
                            this@FullMonthViewRVAdapter.itemCount - parentPosition
                        )

                        Toast.makeText(
                            context,
                            context.getString(
                                R.string.restored_appointment_text,
                                deleteAppointmentModelDb.name
                            ),
                            Toast.LENGTH_LONG
                        ).show()
                    }.show()
            }

            override fun getSwipeEscapeVelocity(defaultValue: Float): Float {
                return super.getSwipeEscapeVelocity(defaultValue) * 10
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                val deleteIcon: Drawable? =
                    ContextCompat.getDrawable(context, R.drawable.baseline_delete_24)!!

                val itemView = viewHolder.itemView
                val iconMarginVertical =
                    (viewHolder.itemView.height - deleteIcon!!.intrinsicHeight) / 2

                val colorDrawableBackground = ColorDrawable(Color.parseColor("#ffcce6"))

                val left =
                    itemView.right - deleteIcon.intrinsicWidth - deleteIcon.intrinsicWidth // 882
                val right = itemView.right - deleteIcon.intrinsicWidth // 1014
                val top = itemView.top + iconMarginVertical
                val bottom = itemView.bottom - iconMarginVertical

                colorDrawableBackground.setBounds(
                    itemView.right + dX.toInt(),
                    itemView.top,
                    itemView.right,
                    itemView.bottom
                )
                deleteIcon.setBounds(left, top, right, bottom)

                deleteIcon.level = 0

                colorDrawableBackground.draw(c)
                c.save()

                if (dX > 0)
                    c.clipRect(itemView.left, itemView.top, dX.toInt(), itemView.bottom)
                else
                    c.clipRect(
                        itemView.right + dX.toInt(),
                        itemView.top,
                        itemView.right,
                        itemView.bottom
                    )

                deleteIcon.draw(c)
                c.restore()

                super.onChildDraw(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
            }
        }).attachToRecyclerView(holder.childRv)
    }

    private fun initClickListener(holder: FullMonthViewRVViewHolder) {
        // create new appointment button
        with(holder) {
            addAppointmentFab.setOnClickListener {
                fullMonthViewViewModel.oldPosition = adapterPosition
                val bundle = Bundle()
                val newAppointment = AppointmentModelDb(
                    date = Util().dateConverterNew(
                        monthDatesList[adapterPosition].date.toString()
                    ), deleted = false
                )

                // set old position to scroll
                fullMonthViewViewModel.oldPosition = holder.adapterPosition

                bundle.putParcelable(bindingKeyAppointment, newAppointment)
                navController.navigate(R.id.action_fullMonthViewFragment_to_nav_appointment, bundle)
            }
        }
    }
}