package com.example.meet_up.domain.repositories

import com.example.meet_up.domain.models.UserModel

interface UserRepository {
    suspend fun getUserByLogin(login: String): Result<UserModel>
    suspend fun getAllUsers(): List<String>
    suspend fun getFilteredUsersByQuery(query: String, users: List<UserModel>): List<UserModel>
    suspend fun getFilteredUsersByEventId(eventId: String): List<UserModel>
}