package com.example.meet_up.domain.interactors

import com.example.meet_up.domain.models.EventModel
import com.example.meet_up.domain.repositories.EventRepository
import com.example.meet_up.domain.repositories.RemoteEventRepository
import javax.inject.Inject

class UpdateEventInteractor @Inject constructor(
    private val eventRepository: EventRepository,
    private val remoteEventRepository: RemoteEventRepository,
) {

    suspend fun invoke(event: EventModel): Result<Unit> {
        val oldParticipantIds = eventRepository.getEvent(event.id).users.map { it.login }
        val newParticipantIds = event.users.map { it.login }

        val result = eventRepository.updateEvent(event)

        newParticipantIds.forEach { newParticipantId ->
            if (!oldParticipantIds.contains(newParticipantId)) {
                eventRepository.addUserToEvent(newParticipantId, event.id)
            }
        }

        oldParticipantIds.forEach { oldParticipantId ->
            if (!newParticipantIds.contains(oldParticipantId)) {
                eventRepository.deleteUserFromEvent(oldParticipantId, event.id)
            }
        }

        remoteEventRepository.putEvent(event)

        return result
    }
}