package com.example.meet_up.presentation.calendar

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.meet_up.MainApplication
import com.example.meet_up.R
import com.example.meet_up.databinding.FragmentCalendarBinding
import com.example.meet_up.presentation.bottom_navigation.BottomNavigationFragmentDirections
import com.example.meet_up.presentation.calendar.calendar_events.DayBinder
import com.example.meet_up.presentation.calendar.calendar_events.MonthsHeaderBinder
import com.example.meet_up.presentation.calendar.adapter.EventAdapter
import com.example.meet_up.tools.hide
import com.example.meet_up.tools.launchWhenStarted
import com.example.meet_up.tools.show
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import kotlinx.coroutines.flow.onEach
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

    private val popUpMenu by lazy {
        PopupMenu(requireContext(), binding.moreButton).apply {
            inflate(R.menu.main_menu)

            setOnMenuItemClickListener {
                return@setOnMenuItemClickListener when (it.itemId) {
                    R.id.menu_item_log_out -> {
                        requireActivity().findNavController(R.id.main_container).run {
                            navigate(BottomNavigationFragmentDirections.actionBottomNavigationFragmentToAuthorizationFragment())
                        }
                        true
                    }

                    else -> false
                }
            }
        }
    }

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
        setupRecyclerView()

        with(binding) {
            setupCalendar()

            addButton.setOnClickListener {
                navController.navigate(CalendarFragmentDirections.actionCalendarFragmentToEditEventFragment())
            }

            moreButton.setOnClickListener {
                popUpMenu.show()
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

    private fun setupRecyclerView() {
        val linearLayoutManager = LinearLayoutManager(context)
        val dividerItemDecoration = DividerItemDecoration(
            context,
            linearLayoutManager.orientation,
        )

        binding.eventsRecycler.apply {
            layoutManager = linearLayoutManager
            adapter = eventAdapter

            addItemDecoration(dividerItemDecoration)
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
        calendarViewModel.loadEventsByDay().onEach { eventsList ->
            eventAdapter.submitList(eventsList)
        }.launchWhenStarted(lifecycleScope)

        calendarViewModel.loadDaysWithEvents().onEach { days ->
            dayBinder.updateDaysWithEvents(days)
            binding.calendarView.notifyCalendarChanged()
        }.launchWhenStarted(lifecycleScope)
    }

    private fun onEventClick(eventId: String) {
        CalendarFragmentDirections.actionCalendarFragmentToEditEventFragment().apply {
            setEventId(eventId)

            navController.navigate(this)
        }
    }

    private fun onDayClick(date: LocalDate) {
        if (calendarViewModel.updateSelectedDay(date)) {
            dayBinder.updateSelectedDay(date)

            binding.apply {
                calendarView.notifyCalendarChanged()
                dayButton.performClick()
            }
        }
    }
}