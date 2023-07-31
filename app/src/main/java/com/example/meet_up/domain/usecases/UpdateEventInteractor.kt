package com.example.meet_up.domain.usecases

import com.example.meet_up.domain.models.EventModel
import com.example.meet_up.domain.repositories.EventRepository
import javax.inject.Inject

class UpdateEventInteractor @Inject constructor(private val eventRepository: EventRepository) {

    suspend fun invoke(eventModel: EventModel) = eventRepository.updateEvent(eventModel)
}