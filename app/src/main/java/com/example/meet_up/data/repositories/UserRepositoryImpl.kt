package com.example.meet_up.data.repositories

import com.example.meet_up.data.local.DataBase
import com.example.meet_up.data.mappers.toUserModel
import com.example.meet_up.domain.models.UserModel
import com.example.meet_up.domain.repositories.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val dataBase: DataBase
): UserRepository {
    override suspend fun getUserByLogin(login: String): Result<UserModel> {
        return dataBase.userDao.getByLogin(login)?.let { user ->
            Result.success(user.toUserModel())
        } ?: Result.failure(Exception(NOT_USER_MESSAGE))
    }

    override suspend fun getAllUsers(): List<String> {
        return dataBase.userDao.getAllUsersName()
    }

    override suspend fun getFilteredUsersByQuery(query: String, users: List<UserModel>): List<UserModel> {
        val logins = users.map { it.login }.toTypedArray()

        return dataBase.userDao.getFilteredUsersByQuery(query, *logins).map { user ->
            user.toUserModel()
        }
    }

    override suspend fun getFilteredUsersByEventId(eventId: String): List<UserModel> {
        return dataBase.userDao.getFilteredUsersByEventId(eventId).map {
            UserModel(it.userLogin, "")
        }
    }

    companion object {
        const val NOT_USER_MESSAGE = "not user with such login"
    }
}