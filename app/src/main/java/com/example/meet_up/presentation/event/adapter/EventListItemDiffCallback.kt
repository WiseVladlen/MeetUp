package com.example.meet_up.presentation.event.adapter

import androidx.recyclerview.widget.DiffUtil

object EventListItemDiffCallback : DiffUtil.ItemCallback<EventListItem>() {

    override fun areItemsTheSame(oldItem: EventListItem, newItem: EventListItem): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: EventListItem, newItem: EventListItem): Boolean {
        return oldItem == newItem
    }
}