package com.example.projectnailsschedule.presentation.settings.themesRV

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.projectnailsschedule.R

class SelectThemeRvAdapter(
    private var themesMap: Map<Int, Pair<String, Int>>
) : RecyclerView.Adapter<SelectThemeRvAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View, listener: OnItemClickListener) :
        RecyclerView.ViewHolder(itemView) {

        lateinit var themeImage: ImageView
        lateinit var themeText: TextView

        init {
            inflateViews()
            setClickListeners(listener)
        }

        private fun inflateViews() {
            with(itemView) {
                themeImage = findViewById(R.id.theme_image)
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
        val view: View = inflater.inflate(R.layout.theme_select_rv_item, parent, false)

        return ViewHolder(view, mListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        inflateViews(holder, position)
    }

    override fun getItemCount(): Int {
        return themesMap.size
    }

    private fun inflateViews(holder: ViewHolder, position: Int) {
        val textTheme = themesMap[position]!!.first
        val resourceIdTheme = themesMap[position]!!.second

        holder.themeText.text = textTheme
        holder.themeImage.setImageResource(resourceIdTheme)
    }
}
