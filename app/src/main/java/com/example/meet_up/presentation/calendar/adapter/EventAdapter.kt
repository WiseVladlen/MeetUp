package com.example.meet_up.presentation.calendar.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter

class EventAdapter(
    private val listener: EventViewHolder.OnEventClickListener,
) : ListAdapter<EventDisplay, EventViewHolder>(EventDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        return EventViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        holder.bind(getItem(position), listener)
    }
}