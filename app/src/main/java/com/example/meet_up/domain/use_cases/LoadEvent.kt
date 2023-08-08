package com.example.meet_up.domain.use_cases

import com.example.meet_up.domain.models.EventModel
import com.example.meet_up.domain.repositories.EventRepository
import javax.inject.Inject

class LoadEvent @Inject constructor(private val eventRepository: EventRepository) {

    suspend operator fun invoke(eventId: String): Result<EventModel> {
        return if (eventId.isEmpty()) {
            Result.failure(Exception())
        } else {
            Result.success(eventRepository.getEvent(eventId))
        }
    }
}