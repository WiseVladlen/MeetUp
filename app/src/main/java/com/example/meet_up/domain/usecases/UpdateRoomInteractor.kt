package com.example.meet_up.domain.usecases

import com.example.meet_up.domain.models.RoomModel
import com.example.meet_up.domain.repositories.RoomRepository
import javax.inject.Inject

class UpdateRoomInteractor @Inject constructor(private val roomRepository: RoomRepository) {

    suspend fun invoke(roomModel: RoomModel) = roomRepository.updateRoom(roomModel)
}