package com.example.meet_up.presentation.event.manage_participant_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.meet_up.domain.models.UserModel
import com.example.meet_up.domain.usecases.LoadFilteredUserListInteractor
import com.example.meet_up.domain.usecases.LoadParticipantListInteractor
import kotlinx.coroutines.CompletableJob
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

class ManageParticipantListViewModel(
    private val loadParticipantListInteractor: LoadParticipantListInteractor,
    private val loadFilteredUserListInteractor: LoadFilteredUserListInteractor,
) : ViewModel() {

    private val loadJob: CompletableJob = SupervisorJob()

    private val _participantListFlow = MutableSharedFlow<List<UserModel>>(1, 1, BufferOverflow.DROP_OLDEST)
    val participantListFlow = _participantListFlow.asSharedFlow()

    private val _userListFlow = MutableSharedFlow<List<UserModel>>(0, 1, BufferOverflow.DROP_OLDEST)
    val userListFlow = _userListFlow.asSharedFlow()

    private val _temporaryParticipantList = mutableListOf<UserModel>()

    val temporaryParticipantList: List<UserModel>
        get() = _temporaryParticipantList

    fun addParticipant(userModel: UserModel) {
        _temporaryParticipantList.add(userModel)

        pushParticipantList(temporaryParticipantList)
    }

    fun removeParticipant(userModel: UserModel) {
        _temporaryParticipantList.remove(userModel)

        pushParticipantList(temporaryParticipantList)
    }

    fun initParticipants(list: List<UserModel>) {
        _temporaryParticipantList.apply {
            clear()
            addAll(list)
        }

        pushParticipantList(list)
    }

    private fun pushParticipantList(list: List<UserModel>) {
        viewModelScope.launch {
            _participantListFlow.emit(list)
        }
    }

    val searchQuery = MutableStateFlow(String())

    init {
        viewModelScope.launch(Dispatchers.IO + loadJob) {
            collectUsersByQuery()
        }
    }

    suspend fun getParticipantList() = loadParticipantListInteractor.invoke("")

    private suspend fun collectUsersByQuery() {
        searchQuery.collectLatest { query ->
            loadFilteredUserListInteractor.invoke(query, temporaryParticipantList)
                .onFailure {  }
                .onSuccess { _userListFlow.emit(it) }
        }
    }

    override fun onCleared() {
        super.onCleared()
        loadJob.cancel()
    }

    @Suppress("UNCHECKED_CAST")
    class ManageParticipantListViewModelFactory @Inject constructor(
        private val loadParticipantListInteractor: Provider<LoadParticipantListInteractor>,
        private val loadFilteredUserListInteractor: Provider<LoadFilteredUserListInteractor>,
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return ManageParticipantListViewModel(
                loadParticipantListInteractor.get(),
                loadFilteredUserListInteractor.get(),
            ) as T
        }
    }
}