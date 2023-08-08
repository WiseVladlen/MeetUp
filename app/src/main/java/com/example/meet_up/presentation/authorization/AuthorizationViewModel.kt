package com.example.meet_up.presentation.authorization

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.meet_up.domain.use_cases.Authorize
import kotlinx.coroutines.CompletableJob
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

class AuthorizationViewModel(
    private val authorize: Authorize,
) : ViewModel() {
    
    private val authJob: CompletableJob = SupervisorJob()

    private val _authorizationProcessFlow = MutableSharedFlow<Boolean>(0, 1, BufferOverflow.DROP_OLDEST)
    val authorizationProcessFlow = _authorizationProcessFlow.asSharedFlow()

    fun authorizationProcess(login: String, password: String) {
        viewModelScope.launch(Dispatchers.IO + authJob) {
            _authorizationProcessFlow.emit(authorize(login, password))
        }
    }

    class AuthorizationViewModelFactory @Inject constructor(
        private val authorize: Provider<Authorize>,
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return AuthorizationViewModel(authorize.get()) as T
        }
    }
}