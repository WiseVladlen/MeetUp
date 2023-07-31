package com.example.meet_up.domain.usecases

import com.example.meet_up.domain.repositories.UserRepository
import javax.inject.Inject

class LoadParticipantListInteractor @Inject constructor(private val userRepository: UserRepository) {

    suspend fun invoke(eventId: String) = userRepository.getFilteredUsersByEventId(eventId)
}