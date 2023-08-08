package com.example.meet_up.presentation.calendar.calendar_events

import android.view.View
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.view.MonthDayBinder
import java.time.LocalDate

class DayBinder(
    private val onDayClickListener: DayViewContainer.OnDayClickListener,
    private var selectedDay: LocalDate = LocalDate.now(),
) : MonthDayBinder<DayViewContainer> {

    private val daysWithEvents = mutableSetOf<LocalDate>()

    override fun bind(container: DayViewContainer, data: CalendarDay) {
        container.bind(
            data,
            DayViewContainer.DayContainerProps(selectedDay, daysWithEvents),
            onDayClickListener
        )
    }

    override fun create(view: View): DayViewContainer = DayViewContainer(view)

    fun updateSelectedDay(newSelectedDay: LocalDate) {
        selectedDay = newSelectedDay
    }

    fun updateDaysWithEvents(newDaysWithEvents: Set<LocalDate>) {
        daysWithEvents.clear()
        daysWithEvents.addAll(newDaysWithEvents)
    }
}