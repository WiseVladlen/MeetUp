package com.example.meet_up.data.mappers

import com.example.meet_up.data.local.entities.Room
import com.example.meet_up.domain.models.RoomModel

fun RoomModel.toRoom(): Room {
    return Room(
        id = id,
        title = title,
    )
}