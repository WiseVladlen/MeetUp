package com.example.meet_up.domain.repositories

import com.example.meet_up.domain.models.RoomModel
import kotlinx.coroutines.flow.Flow

interface RoomRepository {
    suspend fun getRoom(roomId: Int) : RoomModel
    fun getAllRooms(): Flow<List<RoomModel>>
    suspend fun createRoom(roomModel: RoomModel): Result<Unit>
    suspend fun updateRoom(roomModel: RoomModel): Result<Unit>
    suspend fun deleteRoom(roomId: Int): Result<Unit>
}