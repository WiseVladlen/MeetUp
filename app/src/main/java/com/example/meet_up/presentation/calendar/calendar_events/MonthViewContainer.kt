package com.example.meet_up.presentation.calendar.calendar_events

import android.view.View
import android.widget.TextView
import androidx.core.view.children
import com.example.meet_up.R
import com.example.meet_up.databinding.CalendarMonthContainerLayoutBinding
import com.kizitonwose.calendar.core.CalendarMonth
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.view.ViewContainer
import java.time.format.TextStyle
import java.util.Locale

class MonthViewContainer(view: View) : ViewContainer(view) {

    private var _binding: CalendarMonthContainerLayoutBinding

    private val binding
        get() = _binding

    init {
        _binding = CalendarMonthContainerLayoutBinding.bind(view)
    }

    fun bind(date: CalendarMonth) {
        binding.apply {
            textViewDate.text = view.resources.getString(
                R.string.calendar_title,
                date.yearMonth.month.getDisplayName(
                    TextStyle.FULL,
                    Locale.getDefault()
                ),
                date.yearMonth.year
            )

            weekdaysContainer.children.map {
                it as TextView
            }.forEachIndexed { index, textView ->
                val dayOfWeek = daysOfWeek()[index]
                textView.text = dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault())
            }
        }
    }

}