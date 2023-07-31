package com.example.meet_up.domain.repositories

import com.example.meet_up.domain.models.RoomModel
import kotlinx.coroutines.flow.Flow

interface RoomRepository {
    suspend fun createRoom(roomModel: RoomModel)
    suspend fun updateRoom(roomModel: RoomModel)
    suspend fun deleteRoom(roomId: Int)
    fun getAllRooms(): Flow<List<RoomModel>>
}