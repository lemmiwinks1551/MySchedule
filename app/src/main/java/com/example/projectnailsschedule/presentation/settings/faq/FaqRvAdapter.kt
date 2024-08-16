package com.example.projectnailsschedule.presentation.settings.faq

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.projectnailsschedule.R
import com.example.projectnailsschedule.domain.models.FaqModel

class FaqRvAdapter(private val faq: List<FaqModel>) :
    RecyclerView.Adapter<FaqRvAdapter.ViewHolder>() {

    inner class ViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

        var questionTextView: TextView
        var answerTextView: TextView

        init {
            with(itemView) {
                questionTextView = findViewById(R.id.question)
                answerTextView = findViewById(R.id.answer)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.faq_rv_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return faq.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val question = faq[position].question
        val answer = faq[position].answer

        with(holder) {
            questionTextView.text = question
            answerTextView.text = answer
        }
    }
}