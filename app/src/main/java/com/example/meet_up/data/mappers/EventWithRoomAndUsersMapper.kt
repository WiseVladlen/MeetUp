package com.example.meet_up.data.mappers

import com.example.meet_up.data.local.entities.query_models.EventWithRoomAndUsers
import com.example.meet_up.domain.models.EventModel

fun EventWithRoomAndUsers.toEventModel(): EventModel {
    return EventModel(
        id = event.id,
        title = event.title,
        startDate = event.startDate,
        endDate = event.endDate,
        users = users.map { it.toUserModel() },
        room = room.toRoomModel(),
        organizer = event.organizer,
    )
}