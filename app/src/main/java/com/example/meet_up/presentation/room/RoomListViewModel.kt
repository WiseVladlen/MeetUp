package com.example.meet_up.presentation.room

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.meet_up.domain.interactors.LoadRooms
import javax.inject.Inject
import javax.inject.Provider

class RoomListViewModel(loadRooms: LoadRooms): ViewModel() {

    val roomListFlow = loadRooms()

    class RoomListViewModelFactory @Inject constructor(
        private val loadRooms: Provider<LoadRooms>,
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return RoomListViewModel(
                loadRooms.get()
            ) as T
        }
    }
}