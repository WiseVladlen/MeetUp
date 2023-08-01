package com.example.meet_up.domain.repositories

import com.example.meet_up.domain.models.EventModel

interface RemoteEventRepository {
    fun putEvent(eventModel: EventModel)
    fun deleteEvent(eventId: String)
    fun getEventIdList(callback: (IdList: List<String>) -> Unit)
}