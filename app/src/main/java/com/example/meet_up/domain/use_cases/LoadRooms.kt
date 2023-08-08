package com.example.meet_up.domain.use_cases

import com.example.meet_up.domain.repositories.RoomRepository
import javax.inject.Inject

class LoadRooms @Inject constructor(private val roomRepository: RoomRepository) {

    operator fun invoke() = roomRepository.getAllRooms()
}