package com.example.meet_up.presentation.event

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.meet_up.domain.models.RoomModel
import com.example.meet_up.domain.models.UserModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class ManageEventViewModel : ViewModel() {

    private val _roomFlow = MutableSharedFlow<RoomModel>(0, 1, BufferOverflow.DROP_OLDEST)
    val roomFlow = _roomFlow.asSharedFlow()

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