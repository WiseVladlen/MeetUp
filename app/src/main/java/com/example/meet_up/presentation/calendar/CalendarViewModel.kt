package com.example.meet_up.presentation.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.meet_up.domain.interactors.LoadDayListWithEventList
import com.example.meet_up.domain.interactors.LoadEventListByDayInteractor
import com.example.meet_up.presentation.calendar.adapter.EventDisplay
import com.example.meet_up.presentation.mappers.toEventDisplay
import com.example.meet_up.tools.toCalendar
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import java.util.Calendar
import javax.inject.Inject

class CalendarViewModel(
    private val loadEventListByDayInteractor: LoadEventListByDayInteractor,
    private val loadDayListWithEventList: LoadDayListWithEventList,
) : ViewModel() {

    private var selectedDay = Calendar.getInstance()

    fun loadEventsByDay(): Flow<List<EventDisplay>> {
        return loadEventListByDayInteractor(selectedDay).map { eventModels ->
            eventModels.map { eventModel -> eventModel.toEventDisplay() }
        }
    }

    fun loadDaysWithEvents(): Flow<Set<LocalDate>> {
        return loadDayListWithEventList()
    }

    /**
     * @return
     * true, if update success (old selected day and new selected day not equals)
     * false, if update process didn't happen
     */
    fun updateSelectedDay(newSelectedDay: LocalDate): Boolean {
        val newSelectedDayCalendar = newSelectedDay.toCalendar()

        if (selectedDay == newSelectedDayCalendar) return false

        selectedDay = newSelectedDayCalendar.apply {
            set(Calendar.MONTH, get(Calendar.MONTH) - 1)
        }

        loadEventListByDayInteractor.updateDate(selectedDay, viewModelScope)

        return true
    }

    class Factory @Inject constructor(
        private val loadEventListByDayInteractor: LoadEventListByDayInteractor,
        private val loadDayListWithEventList: LoadDayListWithEventList
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return CalendarViewModel(
                loadEventListByDayInteractor,
                loadDayListWithEventList,
            ) as T
        }
    }
}