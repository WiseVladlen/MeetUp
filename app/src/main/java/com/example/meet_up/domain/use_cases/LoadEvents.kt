package com.example.meet_up.domain.use_cases

import com.example.meet_up.data.local.UserStorage
import com.example.meet_up.domain.repositories.EventRepository
import javax.inject.Inject

class LoadEvents @Inject constructor(private val eventRepository: EventRepository) {

    operator fun invoke() = eventRepository.getUserEvents(UserStorage.user.login)
}