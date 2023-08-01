package com.example.meet_up.presentation.event

import androidx.lifecycle.ViewModel
import com.example.meet_up.domain.models.RoomModel
import com.example.meet_up.domain.models.UserModel

class EventConfigViewModel : ViewModel() {

    private var _room: RoomModel? = null

    val room: RoomModel?
        get() = _room

    fun pushRoom(roomModel: RoomModel) {
        _room = roomModel
    }

    private val _temporaryParticipantList = mutableListOf<UserModel>()

    val temporaryParticipantList: List<UserModel>
        get() = _temporaryParticipantList

    fun pushParticipantList(list: List<UserModel>) {
        _temporaryParticipantList.apply {
            clear()
            addAll(list)
        }
    }
}