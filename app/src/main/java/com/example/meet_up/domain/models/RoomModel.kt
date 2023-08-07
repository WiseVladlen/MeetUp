package com.example.meet_up.domain.models

data class RoomModel(
    val id: Int = DEFAULT_ID,
    val title: String,
) {
    companion object {
        const val DEFAULT_ID = 0
    }
}