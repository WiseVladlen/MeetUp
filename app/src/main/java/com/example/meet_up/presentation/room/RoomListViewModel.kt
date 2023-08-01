package com.example.meet_up.presentation.room

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.meet_up.domain.interactors.LoadRoomListInteractor
import javax.inject.Inject
import javax.inject.Provider

class RoomListViewModel(loadRoomListInteractor: LoadRoomListInteractor): ViewModel() {

    val roomListFlow = loadRoomListInteractor.invoke()

    class RoomListViewModelFactory @Inject constructor(
        private val loadRoomListInteractor: Provider<LoadRoomListInteractor>,
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return RoomListViewModel(
                loadRoomListInteractor.get()
            ) as T
        }
    }
}