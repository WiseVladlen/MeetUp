package com.example.meet_up.presentation.calendar.calendarEvents

import android.view.View
import androidx.core.view.isVisible
import com.example.meet_up.databinding.CalendarDayLayoutBinding
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
        onDayClickListener: OnDayClickListener
    ) {
        binding.apply {
            if (date.position == DayPosition.MonthDate) {
                selectedIndicator.isVisible = date.date == dayContainerProps.selectedDate

                val result = dayContainerProps.daysWithEvents.contains(
                    date.date
                )

                eventsIndicator.isVisible = result
                calendarDayText.text = date.date.dayOfMonth.toString()

                root.setOnClickListener {
                    onDayClickListener(date.date)
                }
            } else {
                selectedIndicator.isVisible = false
                eventsIndicator.isVisible = false
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