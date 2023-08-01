package com.example.meet_up.presentation.mappers

import com.example.meet_up.domain.models.EventModel
import com.example.meet_up.presentation.calendar.adapter.EventDisplay

fun EventModel.toEventDisplay(): EventDisplay {
    return EventDisplay(
        id = id,
        title = title,
        roomTitle = room.title,
    )
}