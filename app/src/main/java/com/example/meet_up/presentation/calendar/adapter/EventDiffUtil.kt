package com.example.meet_up.presentation.calendar.adapter

import androidx.recyclerview.widget.DiffUtil

object EventDiffUtil: DiffUtil.ItemCallback<EventDisplay>() {
    override fun areItemsTheSame(oldItem: EventDisplay, newItem: EventDisplay): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: EventDisplay, newItem: EventDisplay): Boolean {
        return oldItem == newItem
    }
}