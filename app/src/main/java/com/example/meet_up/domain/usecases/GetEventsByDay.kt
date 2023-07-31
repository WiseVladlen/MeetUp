package com.example.meet_up.domain.usecases

import com.example.meet_up.domain.models.EventModel
import com.example.meet_up.domain.repositories.EventRepository
import com.example.meet_up.tools.getEndOfDayCalendar
import com.example.meet_up.tools.getStartOfDayCalendar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

class GetEventsByDay @Inject constructor(
    private val eventRepository: EventRepository
) {
    private val eventsFlow = MutableSharedFlow<List<EventModel>>(1, 1, BufferOverflow.DROP_OLDEST)
    private var eventsFlowJob: Job? = null

    @OptIn(DelicateCoroutinesApi::class)
    operator fun invoke(
        dayCalendar: Calendar,
        scope: CoroutineScope = GlobalScope
    ): Flow<List<EventModel>> {
        updateDate(dayCalendar, scope)
        return eventsFlow
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun updateDate(dayCalendar: Calendar, scope: CoroutineScope = GlobalScope) {
        eventsFlowJob?.cancel()
        eventsFlowJob = scope.launch(Dispatchers.IO) {
            eventRepository.getEventsByDate(
                dayCalendar.getStartOfDayCalendar().time,
                dayCalendar.getEndOfDayCalendar().time
            ).collect { eventModels ->
                eventsFlow.emit(eventModels)
            }
        }
    }
}