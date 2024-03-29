package com.example.meet_up.domain.models

import java.util.Date
import java.util.UUID

data class EventModel(
    val id: String = String().validate(),
    val title: String,
    val startDate: Date,
    val endDate: Date,
    val users: List<UserModel>,
    val room: RoomModel,
    val organizer: String,
) {
    companion object {
        fun String.validate() : String {
            return this.ifEmpty {
                UUID.randomUUID().toString()
            }
        }
    }
}