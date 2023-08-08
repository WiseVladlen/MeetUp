package com.example.meet_up.presentation.calendar.calendar_events

import android.view.View
import com.kizitonwose.calendar.core.CalendarMonth
import com.kizitonwose.calendar.view.MonthHeaderFooterBinder

object MonthsHeaderBinder: MonthHeaderFooterBinder<MonthViewContainer> {

    override fun bind(container: MonthViewContainer, data: CalendarMonth) {
        container.bind(data)
    }

    override fun create(view: View): MonthViewContainer = MonthViewContainer(view)
}