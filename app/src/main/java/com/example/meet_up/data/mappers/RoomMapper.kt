package com.example.meet_up.data.mappers

import com.example.meet_up.data.local.entities.Room
import com.example.meet_up.domain.models.RoomModel

fun Room.toRoomModel(): RoomModel {
    return RoomModel(
        id = id,
        title = title
    )
}