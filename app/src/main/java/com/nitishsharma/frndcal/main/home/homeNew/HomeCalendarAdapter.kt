package com.nitishsharma.frndcal.main.home.homeNew

import android.annotation.SuppressLint
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.nitishsharma.frndcal.R

@RequiresApi(Build.VERSION_CODES.O)

class CalendarAdapter(
    private val daysOfMonth: ArrayList<String>,
    private val onItemListener: OnItemListener
) :
    RecyclerView.Adapter<CalendarAdapter.CalendarViewHolder>() {
    var previousSelectedPosition: Int = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.reycler_calendar_cell_item, parent, false)
        return CalendarViewHolder(view, onItemListener)
    }

    override fun onBindViewHolder(holder: CalendarViewHolder, position: Int) {
        if (daysOfMonth[position] == "") {
            holder.dayOfMonth.visibility = View.GONE
        }
        holder.dayOfMonth.text = if (daysOfMonth[position].length < 2) {
            "0${daysOfMonth[position]}"
        } else {
            daysOfMonth[position]
        }
    }

    override fun getItemCount(): Int {
        return daysOfMonth.size
    }

    interface OnItemListener {
        fun onItemClick(position: Int, dayText: String?)
    }

    inner class CalendarViewHolder(itemView: View, onItemListener: OnItemListener) :
        RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val dayOfMonth: TextView
        private val onItemListener: OnItemListener

        init {
            dayOfMonth = itemView.findViewById(R.id.cellDayText)
            this.onItemListener = onItemListener
            itemView.setOnClickListener(this)
        }

        @SuppressLint("ResourceAsColor")
        @RequiresApi(Build.VERSION_CODES.O)
        override fun onClick(view: View) {
            val position = adapterPosition
            val dayText = dayOfMonth.text.toString()

            previousSelectedPosition = if (previousSelectedPosition == -1) {
                itemView.setBackgroundResource(R.drawable.calendar_cell_bg_selected)
                val dayTextView = itemView.findViewById<TextView>(R.id.cellDayText)
                dayTextView.setTextColor(
                    ContextCompat.getColor(
                        itemView.context,
                        R.color.calendarCellColorText
                    )
                )
                position
            } else {
                val previousView =
                    (itemView.parent as RecyclerView).findViewHolderForAdapterPosition(
                        previousSelectedPosition
                    )?.itemView
                previousView?.setBackgroundResource(R.drawable.calendar_cell_bg)
                val previousTextView = previousView?.findViewById<TextView>(R.id.cellDayText)
                previousTextView?.setTextColor(
                    ContextCompat.getColor(
                        previousTextView.context,
                        R.color.calendarPrim
                    )
                )

                val dayTextView = itemView.findViewById<TextView>(R.id.cellDayText)
                dayTextView.setTextColor(
                    ContextCompat.getColor(
                        itemView.context,
                        R.color.calendarCellColorText
                    )
                )
                itemView.setBackgroundResource(R.drawable.calendar_cell_bg_selected)
                position
            }

            onItemListener.onItemClick(position, dayText)
        }

    }

}
