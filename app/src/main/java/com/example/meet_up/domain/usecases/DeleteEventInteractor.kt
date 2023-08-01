package com.example.meet_up.domain.usecases

import com.example.meet_up.domain.repositories.EventRepository
import com.example.meet_up.domain.repositories.RemoteEventRepository
import javax.inject.Inject

class DeleteEventInteractor @Inject constructor(
    private val eventRepository: EventRepository,
    private val remoteEventRepository: RemoteEventRepository,
) {

    suspend fun invoke(eventId: String): Result<Unit> {
        eventRepository.getEvent(eventId).users.forEach {
            eventRepository.deleteUserFromEvent(it.login, eventId)
        }

        remoteEventRepository.deleteEvent(eventId)

        return eventRepository.deleteEvent(eventId)
    }
}