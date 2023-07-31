package com.example.meet_up.presentation.event.manage_participant_list.adapter

import androidx.recyclerview.widget.DiffUtil

object PeopleListItemDiffCallback : DiffUtil.ItemCallback<PeopleListItem>() {

    override fun areItemsTheSame(oldItem: PeopleListItem, newItem: PeopleListItem): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: PeopleListItem, newItem: PeopleListItem): Boolean {
        return oldItem == newItem
    }
}