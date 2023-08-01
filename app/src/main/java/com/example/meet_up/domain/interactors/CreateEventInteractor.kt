package com.example.meet_up.domain.interactors

import com.example.meet_up.domain.models.EventModel
import com.example.meet_up.domain.repositories.EventRepository
import com.example.meet_up.domain.repositories.RemoteEventRepository
import javax.inject.Inject

class CreateEventInteractor @Inject constructor(
    private val eventRepository: EventRepository,
    private val remoteEventRepository: RemoteEventRepository,
) {

    suspend fun invoke(eventModel: EventModel): Result<Unit> {
        val result = eventRepository.createEvent(eventModel)

        eventModel.users.forEach {
            eventRepository.addUserToEvent(it.login, eventModel.id)
        }

        remoteEventRepository.putEvent(eventModel)

        return result
    }
}