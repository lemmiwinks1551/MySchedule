package com.example.projectnailsschedule.presentation.procedures.proceduresRv

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.projectnailsschedule.R
import com.example.projectnailsschedule.domain.models.ProcedureModelDb

class ProceduresAdapter(
    private var proceduresCount: Int,
    private var proceduresList: List<ProcedureModelDb>
) : RecyclerView.Adapter<ProceduresViewHolder>() {

    private lateinit var mListener: OnItemClickListener

    interface OnItemClickListener {
        fun onItemClick(position: Int) {
        }
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProceduresViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.procedure_rv_item, parent, false)

        return ProceduresViewHolder(view, mListener)
    }

    override fun getItemCount(): Int {
        return proceduresCount
    }

    override fun onBindViewHolder(holder: ProceduresViewHolder, position: Int) {
        clearViews(holder)
        setTextViews(holder)
    }

    private fun setTextViews(holder: ProceduresViewHolder) {
        with(proceduresList[holder.adapterPosition]) {
            holder.procedureName.text = procedureName
            holder.procedurePrice.text = procedurePrice
            holder.procedureNotes.text = procedureNotes
        }

        hideEmptyViews(holder, holder.adapterPosition)
    }

    private fun hideEmptyViews(holder: ProceduresViewHolder, position: Int) {
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

    private fun clearViews(holder: ProceduresViewHolder) {
        with(holder) {
            procedureName.visibility = View.VISIBLE
            procedurePrice.visibility = View.VISIBLE
            procedureNotes.visibility = View.VISIBLE
        }
    }

}