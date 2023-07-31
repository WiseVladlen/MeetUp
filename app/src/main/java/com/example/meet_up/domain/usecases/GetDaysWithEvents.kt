package com.example.meet_up.domain.usecases

import com.example.meet_up.data.local.UserStorage
import com.example.meet_up.domain.repositories.EventRepository
import com.example.meet_up.tools.dateToLocalDate
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject

class GetDaysWithEvents @Inject constructor(private val eventRepository: EventRepository) {

    operator fun invoke(): Flow<Set<LocalDate>> {
        return eventRepository.getUserEvents(UserStorage.user.login)
            .map { eventModels ->
                hashSetOf<LocalDate>().apply {
                    addAll(eventModels.flatMap { eventModel ->
                        listOf(
                            dateToLocalDate(eventModel.startDate),
                            dateToLocalDate(eventModel.endDate)
                        )
                    })
                }
            }
    }
}