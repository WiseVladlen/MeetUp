package com.example.meet_up.presentation.event

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.meet_up.domain.models.RoomModel
import com.example.meet_up.domain.models.UserModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class EventConfigViewModel : ViewModel() {

    private val _roomFlow = MutableStateFlow<RoomModel?>(null)
    private val _participantListFlow = MutableStateFlow<List<UserModel>>(listOf())

    val roomFlow = _roomFlow.asStateFlow()
    val participantListFlow = _participantListFlow.asStateFlow()

    fun pushRoom(roomModel: RoomModel) {
        viewModelScope.launch {
            _roomFlow.emit(roomModel)
        }
    }

    fun pushParticipantList(list: List<UserModel>) {
        viewModelScope.launch {
            _participantListFlow.emit(list.toList())
        }
    }
}