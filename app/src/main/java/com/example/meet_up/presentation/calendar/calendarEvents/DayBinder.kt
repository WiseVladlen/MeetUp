package com.example.meet_up.presentation.calendar.calendarEvents

import android.view.View
import com.example.meet_up.domain.usecases.GetDaysWithEvents
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.view.MonthDayBinder
import java.time.LocalDate
import java.util.Calendar

class DayBinder(
    private val onDayClickListener: DayViewContainer.OnDayClickListener,
    private var selectedDay: LocalDate = LocalDate.now()
) : MonthDayBinder<DayViewContainer> {
    private val daysWithEvents: MutableSet<LocalDate> = mutableSetOf()
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