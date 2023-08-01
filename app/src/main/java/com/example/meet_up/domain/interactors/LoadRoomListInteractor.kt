package com.example.meet_up.domain.interactors

import com.example.meet_up.domain.repositories.RoomRepository
import javax.inject.Inject

class LoadRoomListInteractor @Inject constructor(private val roomRepository: RoomRepository) {

    fun invoke() = roomRepository.getAllRooms()
}