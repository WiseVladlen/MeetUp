package com.example.meet_up.domain.usecases

import com.example.meet_up.data.local.UserStorage
import com.example.meet_up.domain.repositories.UserRepository
import javax.inject.Inject

class Authorization @Inject constructor(private val userRepository: UserRepository) {

    suspend operator fun invoke(login: String, password: String): Boolean {
        val userResult = userRepository.getUserByLogin(login)
        userResult.onSuccess { userModel ->
            return (password == userModel.password).also {
                UserStorage.save(userModel)
            }
        }.onFailure {
            return false
        }
        return false
    }
}