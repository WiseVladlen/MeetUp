package com.example.meet_up.presentation.calendar.dayEventsAdapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.meet_up.R

class EventAdapter(
    private val onEventClickListener: EventViewHolder.OnEventClickListener,
) : ListAdapter<EventDisplay, EventViewHolder>(EventDiffUtil) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        return EventViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.event_card_layout, parent, false)
        )
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        holder.bind(getItem(position), onEventClickListener)
    }
}