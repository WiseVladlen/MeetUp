package com.example.meet_up.presentation.authorization

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.meet_up.domain.interactors.AuthorizeInteractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

class AuthorizationViewModel(
    private val authorizeInteractor: AuthorizeInteractor,
) : ViewModel() {

    private val _authorizationProcessLiveData = MutableLiveData<Boolean>()
    val authorizationProcessLiveData: LiveData<Boolean>
        get() = _authorizationProcessLiveData

    fun authorizationProcess(login: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val authorizationResult = authorizeInteractor(login, password)
            _authorizationProcessLiveData.postValue(authorizationResult)
        }
    }

    class AuthorizationViewModelFactory @Inject constructor(
        private val authorizeInteractor: Provider<AuthorizeInteractor>,
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return AuthorizationViewModel(authorizeInteractor.get()) as T
        }
    }
}