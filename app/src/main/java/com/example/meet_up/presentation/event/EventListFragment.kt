package com.example.meet_up.presentation.event

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.meet_up.MainApplication
import com.example.meet_up.R
import com.example.meet_up.databinding.FragmentEventListBinding
import com.example.meet_up.presentation.event.adapter.EventListDelegationAdapter
import com.example.meet_up.presentation.event.adapter.EventListItem
import com.example.meet_up.tools.launchWhenCreated
import kotlinx.coroutines.flow.onEach
import java.time.ZoneId
import javax.inject.Inject

class EventListFragment: Fragment(R.layout.fragment_event_list) {

    @Inject
    lateinit var eventListViewModelFactory: EventListViewModel.EventListViewModelFactory
    private val viewModel by viewModels<EventListViewModel> { eventListViewModelFactory }

    private val eventListAdapter = EventListDelegationAdapter(::onEventItemClick)

    private val navController by lazy { requireActivity().findNavController(R.id.menu_container) }

    private val binding by viewBinding<FragmentEventListBinding>()

    override fun onAttach(context: Context) {
        MainApplication.INSTANCE.mainComponent.inflate(this)

        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeView()
        observeModel()
    }

    private fun initializeView() {
        setupRecyclerView()
    }

    private fun observeModel() {
        viewModel.eventListFlow.onEach { list ->
            with(binding) {
                if ((list.isEmpty() && eventListViewSwitcher.currentView.id == eventListRecyclerView.id) ||
                    (list.isNotEmpty() && eventListViewSwitcher.currentView.id == textViewEmpty.id)) {
                    eventListViewSwitcher.showNext()
                }

                val eventsByDay = list.groupBy {
                    it.startDate.toInstant().atZone(ZoneId.systemDefault()).dayOfMonth
                }.toSortedMap()

                mutableListOf<EventListItem>().apply {
                    eventsByDay.forEach { (_, events) ->
                        add(EventListItem.DateItem(events.first().startDate))

                        events.forEach { event ->
                            add(EventListItem.EventItem(event))
                        }
                    }

                    eventListAdapter.items = this
                }
            }
        }.launchWhenCreated(viewLifecycleOwner)
    }

    private fun setupRecyclerView() {
        val linearLayoutManager = LinearLayoutManager(context)
        val dividerItemDecoration = DividerItemDecoration(
            context,
            linearLayoutManager.orientation,
        )

        binding.eventListRecyclerView.apply {
            layoutManager = linearLayoutManager
            adapter = eventListAdapter

            addItemDecoration(dividerItemDecoration)
        }
    }

    private fun onEventItemClick(item: EventListItem.EventItem) {
        EventListFragmentDirections.actionEventListFragmentToEditEventFragment().apply {
            eventId = item.event.id

            navController.navigate(this)
        }
    }
}