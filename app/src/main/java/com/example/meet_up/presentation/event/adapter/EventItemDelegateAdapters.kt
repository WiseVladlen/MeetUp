package com.example.meet_up.presentation.event.adapter

import com.example.meet_up.R
import com.example.meet_up.databinding.EventCardLayoutBinding
import com.example.meet_up.tools.toFullFormat
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding
import java.text.DateFormat

fun dateAdapterDelegate() = adapterDelegateViewBinding<EventListItem.DateItem, EventListItem, EventCardLayoutBinding>(
    { layoutInflater, parent -> EventCardLayoutBinding.inflate(layoutInflater, parent, false) }
) {
    bind {
        binding.root.text = item.date.toFullFormat()
    }
}

fun eventAdapterDelegate(onLayoutClick: (EventListItem.EventItem) -> Unit) = adapterDelegateViewBinding<EventListItem.EventItem, EventListItem, EventCardLayoutBinding>(
    { layoutInflater, parent -> EventCardLayoutBinding.inflate(layoutInflater, parent, false) }
) {
    bind {
        binding.root.apply {
            text = getString(R.string.event_card_title, item.event.title, item.event.room.title)

            setOnClickListener { onLayoutClick(item) }
        }
    }
}