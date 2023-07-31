package com.example.meet_up.presentation.calendar

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.meet_up.MainApplication
import com.example.meet_up.R
import com.example.meet_up.databinding.FragmentCalendarBinding
import com.example.meet_up.presentation.calendar.calendarEvents.DayBinder
import com.example.meet_up.presentation.calendar.calendarEvents.MonthsHeaderBinder
import com.example.meet_up.presentation.calendar.dayEventsAdapter.EventAdapter
import com.example.meet_up.tools.hide
import com.example.meet_up.tools.launchWhenStarted
import com.example.meet_up.tools.show
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth
import javax.inject.Inject

class CalendarFragment : Fragment(R.layout.fragment_calendar) {

    @Inject
    lateinit var calendarViewModelFactory: CalendarViewModel.Factory
    private val calendarViewModel by viewModels<CalendarViewModel> { calendarViewModelFactory }

    private val navController by lazy { requireActivity().findNavController(R.id.menu_container) }

    private val binding by viewBinding<FragmentCalendarBinding>()

    private val eventAdapter = EventAdapter(::onEventClick)
    private val dayBinder = DayBinder(::onDayClick)

    override fun onAttach(context: Context) {
        MainApplication.INSTANCE.mainComponent.inflate(this)

        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeViews()
        observeModel()
    }

    private fun initializeViews() {
        binding.apply {
            eventsRecycler.adapter = eventAdapter
            setupCalendar()

            addButton.setOnClickListener {
                navController.navigate(CalendarFragmentDirections.actionCalendarFragmentToCreateEventFragment())
            }

            dayButton.setOnClickListener {
                eventsRecycler.show()
                calendarView.hide()
            }

            monthButton.setOnClickListener {
                eventsRecycler.hide()
                calendarView.show()
            }
        }
    }

    private fun FragmentCalendarBinding.setupCalendar() {
        calendarView.dayBinder = dayBinder
        calendarView.monthHeaderBinder = MonthsHeaderBinder

        val currentMonth = YearMonth.now()
        val startMonth = currentMonth.minusMonths(100)  // Adjust as needed
        val endMonth = currentMonth.plusMonths(100)  // Adjust as needed
        val firstDayOfWeek = firstDayOfWeekFromLocale() // Available from the library

        calendarView.setup(startMonth, endMonth, firstDayOfWeek)
        calendarView.scrollToMonth(currentMonth)
    }

    private fun observeModel() {
        lifecycleScope.launch(Dispatchers.Main) {
            calendarViewModel.loadEventsByDay().onEach { eventsList ->
                eventAdapter.submitList(eventsList)
            }.launchWhenStarted(lifecycleScope)

            calendarViewModel.loadDaysWithEvents().onEach { days ->
                dayBinder.updateDaysWithEvents(days)
                binding.calendarView.notifyCalendarChanged()
            }.launchWhenStarted(lifecycleScope)
        }
    }

    private fun onEventClick(eventId: String) {
        navController.navigate(CalendarFragmentDirections.actionCalendarFragmentToEditEventFragment(eventId))
    }

    private fun onDayClick(date: LocalDate) {
        if (calendarViewModel.updateSelectedDay(date)) {
            dayBinder.updateSelectedDay(date)
            binding.calendarView.notifyCalendarChanged()
        }
    }
}