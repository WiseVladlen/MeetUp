package com.example.meet_up.domain.use_cases

import com.example.meet_up.domain.repositories.RoomRepository
import javax.inject.Inject

class DeleteRoom @Inject constructor(private val roomRepository: RoomRepository) {

    suspend operator fun invoke(roomId: Int) = roomRepository.deleteRoom(roomId)
}