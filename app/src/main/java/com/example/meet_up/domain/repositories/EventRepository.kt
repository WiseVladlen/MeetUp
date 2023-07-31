package com.example.meet_up.domain.repositories

import com.example.meet_up.domain.models.EventModel
import kotlinx.coroutines.flow.Flow
import java.util.Date

interface EventRepository {
    suspend fun getEvent(eventId: String): EventModel
    suspend fun createEvent(eventModel: EventModel): Result<Unit>
    suspend fun updateEvent(eventModel: EventModel): Result<Unit>
    suspend fun deleteEvent(eventId: String): Result<Unit>
    suspend fun addUserToEvent(userLogin: String, eventId: String)
    suspend fun deleteUserFromEvent(userLogin: String, eventId: String)
    fun getEventsByDate(startDate: Date, endDate: Date): Flow<List<EventModel>>
    fun getUserEvents(userLogin: String): Flow<List<EventModel>>
    fun roomEventsByRoomId(roomId: Int): Flow<List<EventModel>>
}