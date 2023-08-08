package com.example.meet_up.presentation.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.meet_up.domain.use_cases.LoadDaysWithEvents
import com.example.meet_up.domain.use_cases.LoadEventsByDay
import com.example.meet_up.domain.models.EventModel
import com.example.meet_up.tools.toCalendar
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.util.Calendar
import javax.inject.Inject

class CalendarViewModel(
    private val loadEventsByDay: LoadEventsByDay,
    loadDaysWithEvents: LoadDaysWithEvents,
) : ViewModel() {

    private var _selectedDay = Calendar.getInstance()

    val selectedDay: Calendar
        get() = _selectedDay


    val daysWithEventsFlow: Flow<Set<LocalDate>> = loadDaysWithEvents()
    val eventsByDayFlow: Flow<List<EventModel>> = loadEventsByDay(selectedDay)

    /**
     * @return
     * true, if update success (old selected day and new selected day not equals)
     * false, if update process didn't happen
     */
    fun updateSelectedDay(newSelectedDay: LocalDate): Boolean {
        val newSelectedDayCalendar = newSelectedDay.toCalendar()

        if (selectedDay == newSelectedDayCalendar) return false

        _selectedDay = newSelectedDayCalendar.apply {
            set(Calendar.MONTH, get(Calendar.MONTH) - 1)
        }

        loadEventsByDay.updateDate(selectedDay, viewModelScope)

        return true
    }

    class Factory @Inject constructor(
        private val loadEventsByDay: LoadEventsByDay,
        private val loadDaysWithEvents: LoadDaysWithEvents
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return CalendarViewModel(
                loadEventsByDay,
                loadDaysWithEvents,
            ) as T
        }
    }
}