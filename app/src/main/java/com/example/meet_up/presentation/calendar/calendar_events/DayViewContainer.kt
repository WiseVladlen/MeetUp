package com.example.meet_up.presentation.calendar.calendar_events

import android.view.View
import androidx.core.view.isVisible
import com.example.meet_up.databinding.CalendarDayLayoutBinding
import com.example.meet_up.tools.hide
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.view.ViewContainer
import java.time.LocalDate

class DayViewContainer(view: View) : ViewContainer(view) {

    private var _binding: CalendarDayLayoutBinding? = null

    private val binding
        get() = _binding!!

    init {
        _binding = CalendarDayLayoutBinding.bind(view)
    }

    fun bind(
        date: CalendarDay,
        dayContainerProps: DayContainerProps,
        onDayClickListener: OnDayClickListener,
    ) {
        binding.apply {
            if (date.position == DayPosition.MonthDate) {
                selectedIndicator.isVisible = date.date == dayContainerProps.selectedDate

                eventsIndicator.isVisible = dayContainerProps.daysWithEvents.contains(date.date)
                calendarDayText.text = date.date.dayOfMonth.toString()

                root.setOnClickListener {
                    onDayClickListener(date.date)
                }
            } else {
                selectedIndicator.hide()
                eventsIndicator.hide()
                calendarDayText.text = String()

                root.setOnClickListener(null)
            }
        }
    }

    @FunctionalInterface
    fun interface OnDayClickListener {
        operator fun invoke(day: LocalDate)
    }

    class DayContainerProps(
        val selectedDate: LocalDate,
        val daysWithEvents: Set<LocalDate>
    )
}