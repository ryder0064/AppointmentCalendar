package com.example.appointmentcalendar.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.appointmentcalendar.R
import com.example.appointmentcalendar.data.model.ScheduleItem

class ScheduleListAdapter : RecyclerView.Adapter<ScheduleListAdapter.ViewHolder>() {

    private var scheduleList: List<ScheduleItem> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.item_schedule_view, parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (scheduleList.isNotEmpty()) holder.bind(scheduleList[position])
    }

    override fun getItemCount(): Int = scheduleList.size

    fun setData(data: List<ScheduleItem>) {
        scheduleList = data
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val time: TextView = itemView.findViewById(R.id.time)
        private val card: CardView = itemView.findViewById(R.id.cardView)

        fun bind(scheduleItem: ScheduleItem) {
            if (scheduleItem.isAvailable) {
                card.isClickable = true
                card.setCardBackgroundColor(itemView.resources.getColor(R.color.gray))
                time.setTextColor(itemView.resources.getColor(R.color.green))

            } else {
                card.isClickable = false
                card.setCardBackgroundColor(itemView.resources.getColor(R.color.cool_gray))
                time.setTextColor(itemView.resources.getColor(R.color.ivory))
            }
            time.text = scheduleItem.time
        }
    }
}