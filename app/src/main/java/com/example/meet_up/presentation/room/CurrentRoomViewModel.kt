package com.example.meet_up.presentation.room

import androidx.lifecycle.ViewModel
import com.example.meet_up.domain.models.RoomModel

class CurrentRoomViewModel : ViewModel() {

    private lateinit var _currentRoom: RoomModel

    val currentRoom: RoomModel
        get() = _currentRoom

    fun setRoom(roomModel: RoomModel) {
        _currentRoom = roomModel
    }
}