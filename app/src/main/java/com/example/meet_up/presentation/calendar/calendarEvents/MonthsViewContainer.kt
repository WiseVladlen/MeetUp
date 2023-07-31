package com.example.meet_up.presentation.calendar.calendarEvents

import android.annotation.SuppressLint
import android.view.View
import android.widget.TextView
import androidx.core.view.children
import com.example.meet_up.databinding.CalendarDayLayoutBinding
import com.example.meet_up.databinding.CalendarDayTitlesContainerBinding
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.CalendarMonth
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.view.ViewContainer
import java.time.format.TextStyle
import java.util.Locale

class MonthsViewContainer(view: View) : ViewContainer(view) {
    private var _binding: CalendarDayTitlesContainerBinding? = null
    private val binding
        get() = _binding!!

    init {
        _binding = CalendarDayTitlesContainerBinding.bind(view)
    }

    @SuppressLint("SetTextI18n")
    fun bind(date: CalendarMonth) {
        binding.apply {
            if (root.tag == null) {
                root.tag = date.yearMonth
                monthTitle.text = "${date.yearMonth.year} ${
                    date.yearMonth.month.getDisplayName(
                        TextStyle.FULL,
                        Locale.getDefault()
                    )
                }"
                weekDays.children.map {
                    it as TextView
                }.forEachIndexed { index, textView ->
                    val dayOfWeek = daysOfWeek()[index]
                    val title = dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault())
                    textView.text = title
                }
            }
        }
    }

}