package com.example.meet_up.data.mappers

import com.example.meet_up.data.local.entities.Event
import com.example.meet_up.domain.models.EventModel

fun EventModel.toEvent(): Event {
    return Event(
        id = id,
        title = title,
        startDate = startDate,
        endDate = endDate,
        roomId = room.id,
        organizer = organizer,
    )
}