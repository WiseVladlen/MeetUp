package com.example.meet_up.data.repositories

import com.example.meet_up.data.local.DataBase
import com.example.meet_up.data.local.entities.EventUser
import com.example.meet_up.data.mappers.toEvent
import com.example.meet_up.data.mappers.toEventModel
import com.example.meet_up.domain.models.EventModel
import com.example.meet_up.domain.repositories.EventRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Date
import javax.inject.Inject

class EventRepositoryImpl @Inject constructor(
    private val dataBase: DataBase
): EventRepository {

    override suspend fun getEvent(eventId: String): EventModel {
        return dataBase.eventDao.getEvent(eventId).toEventModel()
    }

    override suspend fun createEvent(eventModel: EventModel): Result<Unit> {
        return try {
            dataBase.eventDao.createEvent(eventModel.toEvent())
            Result.success(Unit)
        } catch (exception: Exception) {
            Result.failure(exception)
        }
    }

    override suspend fun updateEvent(eventModel: EventModel): Result<Unit> {
        return try {
            dataBase.eventDao.updateEvent(eventModel.toEvent())
            Result.success(Unit)
        } catch (exception: Exception) {
            Result.failure(exception)
        }
    }

    override suspend fun deleteEvent(eventId: String): Result<Unit> {
        return try {
            dataBase.eventDao.deleteEventById(eventId)
            Result.success(Unit)
        } catch (exception: Exception) {
            Result.failure(exception)
        }
    }

    override suspend fun addUserToEvent(userLogin: String, eventId: String) {
        dataBase.eventDao.addUserToEvent(EventUser(eventId = eventId, userLogin = userLogin))
    }

    override suspend fun deleteUserFromEvent(userLogin: String, eventId: String) {
        dataBase.eventDao.deleteUserFromEvent(eventId, userLogin)
    }

    override fun getEventsByDate(startDate: Date, endDate: Date): Flow<List<EventModel>> {
        return dataBase.eventDao.getEventsByDate(startDate.time, endDate.time).map { events ->
            events.map { event ->
                event.toEventModel()
            }
        }
    }

    override fun getUserEvents(userLogin: String): Flow<List<EventModel>> {
        return dataBase.eventDao.getUserEventsWithRoom(userLogin).map { events ->
            events.map { event ->
                event.eventWithRoomAndUsers.toEventModel()
            }
        }
    }

    override fun roomEventsByRoomId(roomId: Int): Flow<List<EventModel>> {
        return dataBase.eventDao.getEventsByRoomId(roomId).map { events ->
            events.map { event ->
                event.toEventModel()
            }
        }
    }
}