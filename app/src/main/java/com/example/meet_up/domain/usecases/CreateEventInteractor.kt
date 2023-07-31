package com.example.meet_up.domain.usecases

import com.example.meet_up.domain.models.EventModel
import com.example.meet_up.domain.repositories.EventRepository
import javax.inject.Inject

class CreateEventInteractor @Inject constructor(private val eventRepository: EventRepository) {

    suspend fun invoke(eventModel: EventModel): Result<Unit> {
        val result = eventRepository.createEvent(eventModel)

        eventModel.users.forEach {
            eventRepository.addUserToEvent(it.login, eventModel.id)
        }

        return result
    }
}