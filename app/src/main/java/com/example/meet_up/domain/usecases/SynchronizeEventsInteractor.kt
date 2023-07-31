package com.example.meet_up.domain.usecases

import com.example.meet_up.data.local.UserStorage
import com.example.meet_up.domain.repositories.EventRepository
import com.example.meet_up.domain.repositories.RemoteEventRepository
import javax.inject.Inject

class SynchronizeEventsInteractor @Inject constructor(
    private val eventRepository: EventRepository,
    private val remoteEventRepository: RemoteEventRepository,
) {

    suspend fun invoke() {
        eventRepository.getUserEvents(UserStorage.user.login).collect { localEvents ->
            remoteEventRepository.getEventIdList { eventIdList ->
                localEvents.forEach { localEvent ->
                    if (eventIdList.contains(localEvent.id)) {
                        remoteEventRepository.putEvent(localEvent)
                    } else {
                        remoteEventRepository.deleteEvent(localEvent.id)
                    }
                }
            }
        }
    }
}