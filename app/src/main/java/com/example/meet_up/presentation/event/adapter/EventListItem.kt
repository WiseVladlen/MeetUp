package com.example.meet_up.presentation.event.adapter

import com.example.meet_up.domain.models.EventModel
import java.util.Date

sealed class EventListItem {
    class DateItem(val date: Date) : EventListItem()
    class EventItem(val event: EventModel) : EventListItem()
}