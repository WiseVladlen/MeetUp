package com.example.meet_up.domain.interactors

import com.example.meet_up.domain.models.RoomModel
import com.example.meet_up.domain.repositories.RoomRepository
import javax.inject.Inject

class UpdateRoom @Inject constructor(private val roomRepository: RoomRepository) {

    suspend operator fun invoke(roomModel: RoomModel) = roomRepository.updateRoom(roomModel)
}