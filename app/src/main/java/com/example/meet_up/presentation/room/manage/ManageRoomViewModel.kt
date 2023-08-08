package com.example.meet_up.presentation.room.manage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.meet_up.domain.use_cases.CreateRoom
import com.example.meet_up.domain.models.RoomModel
import com.example.meet_up.domain.use_cases.DeleteRoom
import com.example.meet_up.domain.use_cases.LoadRoom
import com.example.meet_up.domain.use_cases.UpdateRoom
import kotlinx.coroutines.CompletableJob
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

class ManageRoomViewModel(
    private val loadRoom: LoadRoom,
    private val createRoom: CreateRoom,
    private val updateRoom: UpdateRoom,
    private val deleteRoom: DeleteRoom,
) : ViewModel() {

    private val putJob: CompletableJob = SupervisorJob()
    private val deleteJob: CompletableJob = SupervisorJob()

    private val _roomFlow = MutableSharedFlow<Result<RoomModel>>(0, 1, BufferOverflow.DROP_OLDEST)

    private val _onErrorFlow = MutableSharedFlow<String>(0, 1, BufferOverflow.DROP_OLDEST)
    private val _onSuccessFlow = MutableSharedFlow<Unit>(0, 1, BufferOverflow.DROP_OLDEST)

    val roomFlow = _roomFlow.take(1).shareIn(
        viewModelScope,
        SharingStarted.Lazily
    )

    val onErrorFlow = _onErrorFlow.asSharedFlow()
    val onSuccessFlow = _onSuccessFlow.asSharedFlow()

    fun requestRoom(roomId: Int) {
        viewModelScope.launch {
            _roomFlow.emit(loadRoom(roomId))
        }
    }

    fun put(roomId: Int, title: String) {
        viewModelScope.launch(Dispatchers.IO + putJob) {
            if (roomId == RoomModel.DEFAULT_ID) {
                createRoom(RoomModel(title = title))
                    .onFailure { _onErrorFlow.emit(it.message.toString()) }
                    .onSuccess { _onSuccessFlow.emit(it) }
            } else {
                updateRoom(RoomModel(roomId, title))
                    .onFailure { _onErrorFlow.emit(it.message.toString()) }
                    .onSuccess { _onSuccessFlow.emit(it) }
            }
        }
    }

    fun delete(roomId: Int) {
        viewModelScope.launch(Dispatchers.IO + deleteJob) {
            deleteRoom(roomId)
                .onFailure { _onErrorFlow.emit(it.message.toString()) }
                .onSuccess { _onSuccessFlow.emit(it) }
        }
    }

    class ManageRoomViewModelFactory @Inject constructor(
        private val loadRoom: Provider<LoadRoom>,
        private val createRoom: Provider<CreateRoom>,
        private val updateRoom: Provider<UpdateRoom>,
        private val deleteRoom: Provider<DeleteRoom>,
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return ManageRoomViewModel(
                loadRoom.get(),
                createRoom.get(),
                updateRoom.get(),
                deleteRoom.get(),
            ) as T
        }
    }
}