package com.example.meet_up.presentation.room.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.meet_up.domain.models.RoomModel
import com.example.meet_up.domain.interactors.AddRoomInteractor
import kotlinx.coroutines.CompletableJob
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Provider

class AddRoomViewModel(private val addRoomInteractor: AddRoomInteractor) : ViewModel() {

    private val jobCreate: CompletableJob = SupervisorJob()

    fun addRoom(name: String, callback: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO + jobCreate) {
            addRoomInteractor.invoke(RoomModel(title = name))
            withContext(Dispatchers.Main) { callback() }
        }
    }

    override fun onCleared() {
        super.onCleared()
        jobCreate.cancel()
    }

    @Suppress("UNCHECKED_CAST")
    class AddRoomViewModelFactory @Inject constructor(
        private val addRoomInteractor: Provider<AddRoomInteractor>,
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return AddRoomViewModel(addRoomInteractor.get()) as T
        }
    }
}