package com.example.meet_up.domain.interactors

import com.example.meet_up.data.local.UserStorage
import com.example.meet_up.domain.repositories.EventRepository
import com.example.meet_up.domain.repositories.RemoteEventRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class SynchronizeEventsInteractor @Inject constructor(
    private val eventRepository: EventRepository,
    private val remoteEventRepository: RemoteEventRepository,
) {

    suspend fun invoke() {
        eventRepository.getUserEvents(UserStorage.user.login).first().let { localEvents ->
            remoteEventRepository.getEventIdList { remoteEventIds ->
                val localEventIds = localEvents.map { it.id }

                remoteEventIds.forEach { remoteEventId ->
                    if (!localEventIds.contains(remoteEventId)) {
                        remoteEventRepository.deleteEvent(remoteEventId)
                    }
                }

                localEvents.forEach { localEvent ->
                    remoteEventRepository.putEvent(localEvent)
                }
            }
        }
    }
}