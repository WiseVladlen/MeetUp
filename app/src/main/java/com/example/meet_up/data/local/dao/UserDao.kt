package com.example.meet_up.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.meet_up.data.local.entities.EventUser
import com.example.meet_up.data.local.entities.User

@Dao
interface UserDao {
    @Query("SELECT * FROM user WHERE user_login = :queryLogin")
    suspend fun getByLogin(queryLogin: String): User?

    @Query("SELECT user_login FROM user")
    suspend fun getAllUsersName(): List<String>

    @Query("SELECT * FROM user WHERE user_login LIKE '%' || :query || '%' AND user_login NOT IN (:logins) ORDER BY LENGTH(user_login) ASC, user_login ASC")
    suspend fun getFilteredUsersByQuery(query: String, vararg logins: String): List<User>

    @Query("SELECT * FROM event_user WHERE event_id = :eventId")
    suspend fun getFilteredUsersByEventId(eventId: String): List<EventUser>
}