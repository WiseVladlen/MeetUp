package com.example.meet_up.domain.interactors

import com.example.meet_up.domain.models.EventModel
import com.example.meet_up.domain.repositories.EventRepository
import javax.inject.Inject

class LoadEventInteractor @Inject constructor(private val eventRepository: EventRepository) {

    suspend fun invoke(eventId: String): Result<EventModel> {
        return if (eventId.isEmpty()) {
            Result.failure(Exception())
        } else {
            Result.success(eventRepository.getEvent(eventId))
        }
    }
}