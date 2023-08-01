package com.example.meet_up.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.meet_up.data.local.entities.*
import com.example.meet_up.data.local.entities.query_models.EventWithRoomAndUsers
import com.example.meet_up.data.local.entities.query_models.UserEventsWithRoom
import kotlinx.coroutines.flow.Flow

@Dao
interface EventDao {

    @Transaction
    @Query("SELECT * FROM event WHERE event_id = :eventId")
    suspend fun getEvent(eventId: String): EventWithRoomAndUsers

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun createEvent(event: Event): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addUserToEvent(eventUser: EventUser): Long

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateEvent(event: Event): Int

    @Transaction
    @Query("SELECT * FROM event_user WHERE user_login == :userLogin")
    fun getUserEventsWithRoom(userLogin: String): Flow<List<UserEventsWithRoom>>

    @Transaction
    @Query("SELECT * FROM event WHERE start_date <= :endDateMs AND end_date >= :startDateMs")
    fun getEventsByDate(startDateMs: Long, endDateMs: Long): Flow<List<EventWithRoomAndUsers>>

    @Transaction
    @Query("SELECT * FROM event WHERE room_id == :roomId")
    fun getEventsByRoomId(roomId: Int): Flow<List<EventWithRoomAndUsers>>

    @Transaction
    @Query("DELETE FROM event WHERE event_id == :eventId")
    suspend fun deleteEventById(eventId: String)

    @Query("DELETE FROM event_user WHERE event_id == :eventId AND user_login == :userLogin")
    suspend fun deleteUserFromEvent(eventId: String, userLogin: String)
}