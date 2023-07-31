package com.example.meet_up.domain.usecases

import com.example.meet_up.data.local.UserStorage
import com.example.meet_up.domain.repositories.EventRepository
import javax.inject.Inject

class LoadEventListInteractor @Inject constructor(private val eventRepository: EventRepository) {
    fun invoke() = eventRepository.getUserEvents(UserStorage.user.login)
}