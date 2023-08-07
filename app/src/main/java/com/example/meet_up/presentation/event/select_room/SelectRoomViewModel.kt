package com.example.meet_up.presentation.event.select_room

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.meet_up.domain.interactors.LoadRooms
import javax.inject.Inject
import javax.inject.Provider

class SelectRoomViewModel(loadRooms: LoadRooms) : ViewModel() {

    val roomListFlow = loadRooms()

    class SelectRoomViewModelFactory @Inject constructor(
        private val loadRooms: Provider<LoadRooms>,
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return SelectRoomViewModel(loadRooms.get()) as T
        }
    }
}