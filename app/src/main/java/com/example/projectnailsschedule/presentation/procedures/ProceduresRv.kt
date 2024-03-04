package com.example.projectnailsschedule.presentation.procedures

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.projectnailsschedule.R
import com.example.projectnailsschedule.domain.models.ProcedureModelDb

class ProceduresRv(
    private var proceduresList: List<ProcedureModelDb>
) : RecyclerView.Adapter<ProceduresRv.ViewHolder>() {
    inner class ViewHolder(
        itemView: View,
        listener: OnItemClickListener
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

    private lateinit var mListener: OnItemClickListener

    interface OnItemClickListener {
        fun onItemClick(position: Int) {
        }
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.procedure_rv_item, parent, false)

        return ViewHolder(view, mListener)
    }

    override fun getItemCount(): Int {
        return proceduresList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        clearViews(holder)
        setTextViews(holder)
    }

    private fun setTextViews(holder: ViewHolder) {
        with(proceduresList[holder.adapterPosition]) {
            holder.procedureName.text = procedureName
            holder.procedurePrice.text = procedurePrice
            holder.procedureNotes.text = procedureNotes
        }

        hideEmptyViews(holder, holder.adapterPosition)
    }

    private fun hideEmptyViews(holder: ViewHolder, position: Int) {
        with(proceduresList[position]) {
            if (procedureName.isNullOrEmpty()) {
                holder.procedureNameCl.visibility = View.GONE
            }
            if (procedurePrice.isNullOrEmpty()) {
                holder.procedurePriceCl.visibility = View.GONE
            }
            if (procedureNotes.isNullOrEmpty()) {
                holder.procedureNotesCl.visibility = View.GONE
            }
        }
    }

    private fun clearViews(holder: ViewHolder) {
        with(holder) {
            procedureName.visibility = View.VISIBLE
            procedurePrice.visibility = View.VISIBLE
            procedureNotes.visibility = View.VISIBLE
        }
    }
}