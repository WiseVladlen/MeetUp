package com.example.meet_up.presentation.calendar.dayEventsAdapter

import androidx.recyclerview.widget.DiffUtil

object EventDiffUtil: DiffUtil.ItemCallback<EventDisplay>() {
    override fun areItemsTheSame(oldItem: EventDisplay, newItem: EventDisplay): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: EventDisplay, newItem: EventDisplay): Boolean {
        return oldItem == newItem
    }
}