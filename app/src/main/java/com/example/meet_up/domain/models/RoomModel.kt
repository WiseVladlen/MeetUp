package com.example.meet_up.domain.models

data class RoomModel(
    val id: Int = 0,
    val title: String
) {
    companion object {
        private const val DEFAULT_ID = 0
    }
}
