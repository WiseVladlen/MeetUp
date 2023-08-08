package com.example.meet_up.data.repositories

import com.example.meet_up.data.local.DataBase
import com.example.meet_up.data.mappers.toRoom
import com.example.meet_up.data.mappers.toRoomModel
import com.example.meet_up.domain.models.RoomModel
import com.example.meet_up.domain.repositories.RoomRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RoomRepositoryImpl @Inject constructor(
    private val dataBase: DataBase,
): RoomRepository {

    override suspend fun createRoom(roomModel: RoomModel): Result<Unit> {
        return try {
            dataBase.roomDao.createRoom(roomModel.toRoom())
            Result.success(Unit)
        } catch (exception: Exception) {
            Result.failure(exception)
        }
    }

    override suspend fun updateRoom(roomModel: RoomModel): Result<Unit> {
        return try {
            dataBase.roomDao.updateRoom(roomModel.toRoom())
            Result.success(Unit)
        } catch (exception: Exception) {
            Result.failure(exception)
        }
    }

    override suspend fun deleteRoom(roomId: Int): Result<Unit> {
        return try {
            dataBase.roomDao.deleteRoomById(roomId)
            Result.success(Unit)
        } catch (exception: Exception) {
            Result.failure(exception)
        }
    }

    override suspend fun getRoom(roomId: Int): RoomModel {
        return dataBase.roomDao.getRoom(roomId).toRoomModel()
    }

    override fun getAllRooms(): Flow<List<RoomModel>> {
        return dataBase.roomDao.getAllRooms().map {  rooms ->
            rooms.map { room ->
                room.toRoomModel()
            }
        }
    }
}