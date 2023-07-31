package com.example.meet_up.domain.usecases

import com.example.meet_up.domain.repositories.EventRepository
import javax.inject.Inject

class DeleteEventInteractor @Inject constructor(private val eventRepository: EventRepository) {

    suspend fun invoke(eventId: String) = eventRepository.deleteEvent(eventId)
}