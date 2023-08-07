package com.example.meet_up.domain.interactors

import com.example.meet_up.domain.models.UserModel
import com.example.meet_up.domain.repositories.UserRepository
import java.lang.Exception
import javax.inject.Inject

class LoadFilteredUsers @Inject constructor(private val userRepository: UserRepository) {

    suspend operator fun invoke(query: String, users: List<UserModel>): Result<List<UserModel>> {
        return if (query.isBlank() || query.isEmpty()) {
            Result.failure(Exception())
        } else {
            Result.success(userRepository.getFilteredUsersByQuery(query, users))
        }
    }
}