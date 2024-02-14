package com.example.projectnailsschedule.presentation.appointment.selectProcedure.selectProcedureRV

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.projectnailsschedule.R
import com.example.projectnailsschedule.domain.models.ProcedureModelDb

class SelectProcedureRVAdapter(
    private var proceduresCount: Int,
    private var proceduresList: List<ProcedureModelDb>
) : RecyclerView.Adapter<SelectProcedureRVAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View, listener: OnItemClickListener) :
        RecyclerView.ViewHolder(itemView) {

        lateinit var procedureNameCl: ConstraintLayout
        lateinit var procedurePriceCl: ConstraintLayout
        lateinit var procedureNotesCl: ConstraintLayout

        lateinit var procedureName: TextView
        lateinit var procedurePrice: TextView
        lateinit var procedureNotes: TextView

        init {
            inflateViews()
            setClickListeners(listener)
        }

        private fun inflateViews() {
            with(itemView) {
                // constraint layouts
                procedureNameCl = findViewById(R.id.procedure_name_cl_select_procedure_item)
                procedurePriceCl = findViewById(R.id.procedure_price_cl_select_procedure_item)
                procedureNotesCl = findViewById(R.id.procedure_notes_cl_select_procedure_item)

                // views
                procedureName = findViewById(R.id.procedure_name_tv_select_procedure_item)
                procedurePrice = findViewById(R.id.procedure_price_tv_select_procedure_item)
                procedureNotes = findViewById(R.id.procedure_notes_tv_select_procedure_item)
            }
        }

        private fun setClickListeners(listener: OnItemClickListener) {
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
        val view: View = inflater.inflate(R.layout.procedure_select_rv_item, parent, false)

        return ViewHolder(view, mListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        inflateViews(holder, position)
    }

    override fun getItemCount(): Int {
        return proceduresCount
    }

    private fun inflateViews(holder: ViewHolder, position: Int) {
        with(proceduresList[position]) {
            holder.procedureName.text = procedureName
            holder.procedurePrice.text = procedurePrice
            holder.procedureNotes.text = procedureNotes
        }

        hideEmptyViews(holder, position)
    }

    private fun hideEmptyViews(holder: ViewHolder, position: Int) {
        if (proceduresList[position].procedureName.isNullOrEmpty()) {
            holder.procedureNameCl.visibility = View.GONE
        }
        if (proceduresList[position].procedurePrice.isNullOrEmpty()) {
            holder.procedurePriceCl.visibility = View.GONE
        }
        if (proceduresList[position].procedureNotes.isNullOrEmpty()) {
            holder.procedureNotesCl.visibility = View.GONE
        }
    }
}
