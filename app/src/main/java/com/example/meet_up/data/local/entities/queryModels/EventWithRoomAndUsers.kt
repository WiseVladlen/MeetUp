package com.example.meet_up.data.local.entities.queryModels

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.meet_up.data.local.entities.Event
import com.example.meet_up.data.local.entities.EventUser
import com.example.meet_up.data.local.entities.Room
import com.example.meet_up.data.local.entities.User

data class EventWithRoomAndUsers(
    @Embedded
    val event: Event,
    @Relation(
        parentColumn = Event.ROOM_ID_COLUMN_NAME,
        entityColumn = Room.ID_COLUMN_NAME
    )
    val room: Room,
    @Relation(
        parentColumn = EventUser.EVENT_ID_COLUMN_NAME,
        entityColumn = EventUser.USER_LOGIN_COLUMN_NAME,
        associateBy = Junction(EventUser::class)
    )
    val users: List<User>
)