package com.example.meet_up.presentation.event

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.meet_up.domain.use_cases.LoadEvents
import javax.inject.Inject
import javax.inject.Provider

class EventListViewModel(loadEvents: LoadEvents) : ViewModel() {

    val eventListFlow = loadEvents()

    class EventListViewModelFactory @Inject constructor(
        private val loadEvents: Provider<LoadEvents>,
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return EventListViewModel(loadEvents.get()) as T
        }
    }
}