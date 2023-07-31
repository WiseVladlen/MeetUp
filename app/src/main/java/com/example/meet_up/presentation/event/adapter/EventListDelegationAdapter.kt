package com.example.meet_up.presentation.event.adapter

import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter

class EventListDelegationAdapter(
    onEventItemClick: (EventListItem.EventItem) -> Unit,
) : AsyncListDifferDelegationAdapter<EventListItem>(
    EventListItemDiffCallback,
    dateAdapterDelegate(),
    eventAdapterDelegate(onEventItemClick),
)