package com.example.meet_up.data.local.entities.query_models

import androidx.room.Embedded
import androidx.room.Relation
import com.example.meet_up.data.local.entities.Event
import com.example.meet_up.data.local.entities.EventUser

data class UserEventsWithRoom(
    @Embedded
    val eventUser: EventUser,
    @Relation(
        parentColumn = EventUser.EVENT_ID_COLUMN_NAME,
        entityColumn = Event.ID_COLUMN_NAME,
        entity = Event::class
    )
    val eventWithRoomAndUsers: EventWithRoomAndUsers,
)