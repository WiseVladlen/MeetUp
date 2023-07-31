package com.example.meet_up.presentation.event

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.meet_up.domain.usecases.LoadEventListInteractor
import javax.inject.Inject
import javax.inject.Provider

class EventListViewModel(loadEventListInteractor: LoadEventListInteractor) : ViewModel() {

    val eventListFlow = loadEventListInteractor.invoke()

    @Suppress("UNCHECKED_CAST")
    class EventListViewModelFactory @Inject constructor(
        private val loadEventListInteractor: Provider<LoadEventListInteractor>,
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return EventListViewModel(loadEventListInteractor.get()) as T
        }
    }
}