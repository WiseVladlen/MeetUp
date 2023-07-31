package com.example.meet_up.domain.usecases

import com.example.meet_up.domain.models.EventModel
import com.example.meet_up.tools.MAX_EVENT_DURATION
import com.example.meet_up.tools.MIN_EVENT_DURATION
import java.lang.Exception
import java.time.Duration
import javax.inject.Inject

class ValidateEventInteractor @Inject constructor() {

    fun invoke(eventModel: EventModel): Result<Unit> {
        val duration = Duration.ofMillis(eventModel.endDate.time - eventModel.startDate.time)

        return if (eventModel.title.isBlank()) {
            Result.failure(Exception(BLANK_TITLE))
        } else if (eventModel.endDate.before(eventModel.startDate)) {
            Result.failure(Exception(END_DATE_BEFORE_START_DATE))
        } else if (duration.toDays().toInt() > MIN_EVENT_DURATION) {
            Result.failure(Exception(EVENT_DURATION_MORE_THAN_POSSIBLE))
        } else if (duration.toMinutes() < MAX_EVENT_DURATION) {
            Result.failure(Exception(EVENT_DURATION_LESS_THAN_POSSIBLE))
        } else {
            Result.success(Unit)
        }
    }

    companion object {
        private const val BLANK_TITLE = "The title cannot be blank"
        private const val END_DATE_BEFORE_START_DATE = "End date before the start date"
        private const val EVENT_DURATION_MORE_THAN_POSSIBLE = "Duration of the event exceeds 1 day"
        private const val EVENT_DURATION_LESS_THAN_POSSIBLE = "Duration of the event cannot be less than 30 minutes"
    }
}