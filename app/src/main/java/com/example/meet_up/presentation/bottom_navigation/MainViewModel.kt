package com.example.meet_up.presentation.bottom_navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.meet_up.domain.use_cases.SynchronizeEvents
import kotlinx.coroutines.CompletableJob
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

class MainViewModel(
    private val synchronizeEvents: SynchronizeEvents,
) : ViewModel() {

    private val syncJob: CompletableJob = SupervisorJob()

    fun synchronize() {
        viewModelScope.launch(Dispatchers.IO + syncJob) {
            synchronizeEvents()
        }
    }
    
    class MainViewModelFactory @Inject constructor(
        private val synchronizeEvents: Provider<SynchronizeEvents>,
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MainViewModel(synchronizeEvents.get()) as T
        }
    }
}