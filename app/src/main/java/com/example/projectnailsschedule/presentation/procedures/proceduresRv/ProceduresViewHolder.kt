package com.example.projectnailsschedule.presentation.procedures.proceduresRv

import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.projectnailsschedule.R

class ProceduresViewHolder internal constructor(
    itemView: View,
    listener: ProceduresAdapter.OnItemClickListener
) : RecyclerView.ViewHolder(itemView) {

    val procedureNameCl: ConstraintLayout
    val procedurePriceCl: ConstraintLayout
    val procedureNotesCl: ConstraintLayout

    val procedureName: TextView
    val procedurePrice: TextView
    val procedureNotes: TextView

    init {
        with(itemView) {
            procedureNameCl = findViewById(R.id.procedure_name_cl_rv_item)
            procedurePriceCl = findViewById(R.id.procedure_price_cl_rv_item)
            procedureNotesCl = findViewById(R.id.procedure_notes_cl_rv_item)

            procedureName = findViewById(R.id.procedure_name_rv_item)
            procedurePrice = findViewById(R.id.procedure_price_rv_item)
            procedureNotes = findViewById(R.id.procedure_notes_rv_item)
        }

        itemView.setOnClickListener {
            listener.onItemClick(adapterPosition)
        }
    }

}