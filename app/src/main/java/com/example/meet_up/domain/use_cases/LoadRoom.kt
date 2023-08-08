package com.example.meet_up.domain.use_cases

import com.example.meet_up.domain.models.RoomModel
import com.example.meet_up.domain.repositories.RoomRepository
import javax.inject.Inject

class LoadRoom @Inject constructor(private val roomRepository: RoomRepository) {

    suspend operator fun invoke(roomId: Int): Result<RoomModel> {
        return if (roomId == RoomModel.DEFAULT_ID) {
            Result.failure(Exception())
        } else {
            Result.success(roomRepository.getRoom(roomId))
        }
    }
}