package com.nitishsharma.frndcal.main.home.homeNew

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.nitishsharma.frndcal.R
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)

class CalendarAdapter(
    private val userSelectedDate: LocalDate,
    private val localDate: LocalDate,
    private val daysOfMonth: ArrayList<String>,
    private val onItemListener: OnItemListener
) :
    RecyclerView.Adapter<CalendarAdapter.ViewHolder>() {
    var previousSelectedPosition: Int = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.reycler_calendar_cell_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (daysOfMonth[position] == "") {
            holder.dayOfMonth.visibility = View.GONE
        }
        holder.dayOfMonth.text = if (daysOfMonth[position].length < 2) {
            "0${daysOfMonth[position]}"
        } else {
            daysOfMonth[position]
        }

        if(daysOfMonth[position] != "") {
            if (userSelectedDate.dayOfMonth == daysOfMonth[position].toInt() && localDate.monthValue == userSelectedDate.monthValue && userSelectedDate.year == localDate.year && userSelectedDate.dayOfMonth == localDate.dayOfMonth) {
                previousSelectedPosition = position
                holder.itemView.setBackgroundResource(R.drawable.calendar_cell_bg_selected)
                val dayTextView = holder.itemView.findViewById<TextView>(R.id.cellDayText)
                dayTextView.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.calendarCellColorText))
            }
        }

        holder.itemView.setOnClickListener {
            previousSelectedPosition = if (previousSelectedPosition == -1) {
                holder.itemView.setBackgroundResource(R.drawable.calendar_cell_bg_selected)
                val dayTextView = holder.itemView.findViewById<TextView>(R.id.cellDayText)
                dayTextView.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.calendarCellColorText))
                position
            } else {
                val previousView = (holder.itemView.parent as RecyclerView).findViewHolderForAdapterPosition(previousSelectedPosition)?.itemView
                previousView?.setBackgroundResource(R.drawable.calendar_cell_bg)
                val previousTextView = previousView?.findViewById<TextView>(R.id.cellDayText)
                previousTextView?.setTextColor(ContextCompat.getColor(previousTextView.context, R.color.calendarPrim))

                val dayTextView = holder.itemView.findViewById<TextView>(R.id.cellDayText)
                dayTextView.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.calendarCellColorText))
                holder.itemView.setBackgroundResource(R.drawable.calendar_cell_bg_selected)
                position
            }

            onItemListener.onItemClick(position, daysOfMonth[position])
        }
    }

    override fun getItemCount(): Int {
        return daysOfMonth.size
    }

    interface OnItemListener {
        fun onItemClick(position: Int, dayText: String?)
    }

    inner class ViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val dayOfMonth: TextView = itemView.findViewById(R.id.cellDayText)
    }
}
