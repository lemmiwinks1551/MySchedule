package com.example.projectnailsschedule.presentation.calendar.fullMonthView.fullMonthViewRV

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.google.android.material.snackbar.Snackbar


class FullMonthViewRVAdapter(
    private val monthDatesList: MutableList<DateWeekAppModel>,
    private val context: Context,
    private val navController: NavController,
    private val fullMonthViewViewModel: FullMonthViewViewModel
) : RecyclerView.Adapter<FullMonthViewRVViewHolder>(
) {
    lateinit var fullMonthChildAdapter: FullMonthChildAdapter
    lateinit var view: View

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

        if (monthDatesList[position].appointmentsList.isEmpty()) {
            // if no appointments in current date
            fillNoAppointmentsTv(holder)
        } else {
            // if appointments exists in current date
            fillAppointmentsExistsTv(holder)
            initChildRv(holder, position)
        }
    }

    private fun fillDateTv(holder: FullMonthViewRVViewHolder) {
        // set values into date and weekName text views
        with(holder) {
            holder.date.text = monthDatesList[position].day
            holder.weekDayName.text = monthDatesList[position].weekDay
        }
    }

    private fun fillNoAppointmentsTv(holder: FullMonthViewRVViewHolder) {
        // fill views if no appointments
        with(holder) {
            noAppointmentsText.visibility = View.VISIBLE
            childRv.visibility = View.GONE
        }
    }

    private fun fillAppointmentsExistsTv(holder: FullMonthViewRVViewHolder) {
        // fill text views if appointments exists
        with(holder) {
            noAppointmentsText.visibility = View.GONE
            childRv.visibility = View.VISIBLE
        }
    }

    private fun initChildRv(holder: FullMonthViewRVViewHolder, position: Int) {
        val parentItem = monthDatesList[position]

        with(holder) {
            childRv.setHasFixedSize(true)
            childRv.layoutManager = GridLayoutManager(holder.itemView.context, 1)

            fullMonthChildAdapter =
                FullMonthChildAdapter(parentItem.appointmentsList, context, navController)
            childRv.adapter = fullMonthChildAdapter
        }

        swipeToDelete(holder, parentItem)
    }

    private fun swipeToDelete(holder: FullMonthViewRVViewHolder, parentItem: DateWeekAppModel) {
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
                // this method is called when we swipe our item to left direction.
                // on below line we are getting the item at a particular position.
                val position = viewHolder.adapterPosition

                val deleteAppointmentModelDb: AppointmentModelDb =
                    (viewHolder as FullMonthChildViewHolder).appointmentModelDb!!

                // delete client from Db
                fullMonthViewViewModel.deleteAppointment(deleteAppointmentModelDb)
                parentItem.appointmentsList.remove(deleteAppointmentModelDb)

                holder.childRv.adapter!!.notifyDataSetChanged()
                fullMonthChildAdapter.notifyDataSetChanged()
                if (parentItem.appointmentsList.isEmpty()) {
                    fillNoAppointmentsTv(holder)
                }
                // show Snackbar
                Snackbar.make(
                    holder.childRv,
                    "Удалена запись: " + deleteAppointmentModelDb.name,
                    Snackbar.LENGTH_LONG
                ).setBackgroundTint(Color.parseColor("#ffff00"))
                    .setActionTextColor(Color.parseColor("#003300"))
                    .setTextColor(Color.parseColor("#003300"))
                    .setAction(
                        "Отмена"
                    ) {
                        // adding on click listener to our action of snack bar.
                        // below line is to add our item to array list with a position.
                        fullMonthViewViewModel.saveAppointment(deleteAppointmentModelDb)
                        //fullMonthChildAdapter.appointmentsList.add(position, deleteAppointmentModelDb)

                        // below line is to notify item is
                        // added to our adapter class.
                        parentItem.appointmentsList.add(position, deleteAppointmentModelDb)

                        holder.childRv.adapter!!.notifyDataSetChanged()
                        fullMonthChildAdapter.notifyDataSetChanged()

                        if (parentItem.appointmentsList.isNotEmpty()) {
                            fillAppointmentsExistsTv(holder)
                        }
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
}