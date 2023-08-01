package com.example.meet_up.presentation.room.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.meet_up.domain.models.RoomModel
import com.example.meet_up.domain.interactors.DeleteRoomInteractor
import com.example.meet_up.domain.interactors.UpdateRoomInteractor
import kotlinx.coroutines.CompletableJob
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Provider

class EditRoomViewModel(
    private val updateRoomInteractor: UpdateRoomInteractor,
    private val deleteRoomInteractor: DeleteRoomInteractor,
) : ViewModel() {

    private val updateJob: CompletableJob = SupervisorJob()
    private val deleteJob: CompletableJob = SupervisorJob()

    fun updateRoom(oldRoomModel: RoomModel, name: String, callback: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO + updateJob) {
            updateRoomInteractor.invoke(RoomModel(oldRoomModel.id, name))
            withContext(Dispatchers.Main) { callback() }
        }
    }

    fun deleteRoom(roomModel: RoomModel, callback: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO + deleteJob) {
            deleteRoomInteractor.invoke(roomModel.id)
            withContext(Dispatchers.Main) { callback() }
        }
    }

    override fun onCleared() {
        super.onCleared()
        updateJob.cancel()
        deleteJob.cancel()
    }

    @Suppress("UNCHECKED_CAST")
    class EditRoomViewModelFactory @Inject constructor(
        private val updateRoomInteractor: Provider<UpdateRoomInteractor>,
        private val deleteRoomInteractor: Provider<DeleteRoomInteractor>,
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return EditRoomViewModel(
                updateRoomInteractor.get(),
                deleteRoomInteractor.get(),
            ) as T
        }
    }
}