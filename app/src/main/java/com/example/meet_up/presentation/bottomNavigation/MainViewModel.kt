package com.example.meet_up.presentation.bottomNavigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.meet_up.domain.usecases.SynchronizeEventsInteractor
import kotlinx.coroutines.CompletableJob
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

class MainViewModel(
    private val synchronizeEventsInteractor: SynchronizeEventsInteractor,
) : ViewModel() {

    private val jobSynchronize: CompletableJob = SupervisorJob()

    fun synchronize() {
        viewModelScope.launch(Dispatchers.IO + jobSynchronize) {
            synchronizeEventsInteractor.invoke()
        }
    }

    override fun onCleared() {
        super.onCleared()
        jobSynchronize.cancel()
    }
    
    class MainViewModelFactory @Inject constructor(
        private val synchronizeEventsInteractor: Provider<SynchronizeEventsInteractor>,
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MainViewModel(synchronizeEventsInteractor.get()) as T
        }
    }
}