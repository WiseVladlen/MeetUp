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
import com.example.meet_up.presentation.mappers.toEventDisplay
import com.example.meet_up.tools.hide
import com.example.meet_up.tools.launchWhenCreated
import com.example.meet_up.tools.launchWhenStarted
import com.example.meet_up.tools.show
import com.example.meet_up.tools.toFullFormat
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import kotlinx.coroutines.flow.onEach
import java.time.LocalDate
import java.time.YearMonth
import javax.inject.Inject

class CalendarFragment : Fragment(R.layout.fragment_calendar) {

    @Inject
    lateinit var calendarViewModelFactory: CalendarViewModel.Factory
    private val viewModel by viewModels<CalendarViewModel> { calendarViewModelFactory }

    private val navController by lazy { requireActivity().findNavController(R.id.menu_container) }

    private val binding by viewBinding<FragmentCalendarBinding>()

    private val eventAdapter = EventAdapter(::onEventClick)
    private val dayBinder = DayBinder(::onDayClick)

    private val popUpMenu by lazy {
        PopupMenu(requireContext(), binding.moreButton).apply {
            inflate(R.menu.main_menu)

            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.menu_item_log_out -> {
                        requireActivity().findNavController(R.id.main_container).run {
                            navigate(BottomNavigationFragmentDirections.actionBottomNavigationFragmentToAuthorizationFragment())
                        }
                    }
                }
                return@setOnMenuItemClickListener true
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
                navController.navigate(CalendarFragmentDirections.actionCalendarFragmentToManageEventGraph())
            }

            moreButton.setOnClickListener {
                popUpMenu.show()
            }

            periodButtonGroup.apply {
                setOnCheckedChangeListener { _, id ->
                    when (id) {
                        dayButton.id -> {
                            eventContainer.show()
                            calendarView.hide()

                            textViewTitle.text = viewModel.selectedDay.time.toFullFormat()
                        }

                        monthButton.id -> {
                            eventContainer.hide()
                            calendarView.show()
                        }
                    }
                }

                check(dayButton.id)
            }
        }
    }

    private fun setupRecyclerView() {
        val linearLayoutManager = LinearLayoutManager(context)
        val dividerItemDecoration = DividerItemDecoration(
            context,
            linearLayoutManager.orientation,
        )

        binding.eventListRecyclerView.apply {
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
        viewModel.eventsByDayFlow.onEach { list ->
            with(binding) {
                if ((list.isEmpty() && eventListViewSwitcher.currentView.id == eventListRecyclerView.id) || eventListViewSwitcher.currentView.id == textViewEmpty.id) {
                    eventListViewSwitcher.showNext()
                }

                textViewTitle.text = viewModel.selectedDay.time.toFullFormat()
                eventAdapter.submitList(list.map { it.toEventDisplay() })
            }
        }.launchWhenCreated(viewLifecycleOwner)

        viewModel.daysWithEventsFlow.onEach { days ->
            dayBinder.updateDaysWithEvents(days)
            binding.calendarView.notifyCalendarChanged()
        }.launchWhenStarted(lifecycleScope)
    }

    private fun onEventClick(eventId: String) {
        CalendarFragmentDirections.actionCalendarFragmentToManageEventGraph().apply {
            setEventId(eventId)

            navController.navigate(this)
        }
    }

    private fun onDayClick(date: LocalDate) {
        if (viewModel.updateSelectedDay(date)) {
            dayBinder.updateSelectedDay(date)

            binding.apply {
                calendarView.notifyCalendarChanged()
                dayButton.performClick()
            }
        }
    }
}