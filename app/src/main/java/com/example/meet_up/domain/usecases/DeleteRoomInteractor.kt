package com.example.meet_up.domain.usecases

import com.example.meet_up.domain.repositories.RoomRepository
import javax.inject.Inject

class DeleteRoomInteractor @Inject constructor(private val roomRepository: RoomRepository) {

    suspend fun invoke(roomId: Int) = roomRepository.deleteRoom(roomId)
}