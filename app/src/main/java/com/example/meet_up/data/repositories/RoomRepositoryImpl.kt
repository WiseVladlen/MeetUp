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
    private val dataBase: DataBase
): RoomRepository {

    override suspend fun createRoom(roomModel: RoomModel) {
        dataBase.roomDao.createRoom(roomModel.toRoom())
    }

    override suspend fun updateRoom(roomModel: RoomModel) {
        dataBase.roomDao.updateRoom(roomModel.toRoom())
    }

    override suspend fun deleteRoom(roomId: Int) {
        dataBase.roomDao.deleteRoomById(roomId)
    }

    override fun getAllRooms(): Flow<List<RoomModel>> {
        return dataBase.roomDao.getAllRooms().map {  rooms ->
            rooms.map { room ->
                room.toRoomModel()
            }
        }
    }
}