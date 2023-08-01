package com.example.meet_up.domain.interactors

import com.example.meet_up.data.local.UserStorage
import com.example.meet_up.data.remote.HttpClientFactory
import com.example.meet_up.domain.repositories.UserRepository
import javax.inject.Inject

class AuthorizeInteractor @Inject constructor(
    private val userRepository: UserRepository,
    private val httpClientFactory: HttpClientFactory,
) {

    suspend operator fun invoke(login: String, password: String): Boolean {
        userRepository.getUserByLogin(login)
            .onSuccess { userModel ->
                return (password == userModel.password).also {
                    UserStorage.save(userModel)
                    httpClientFactory.create()
                }
            }.onFailure {
                return false
            }
        return false
    }
}