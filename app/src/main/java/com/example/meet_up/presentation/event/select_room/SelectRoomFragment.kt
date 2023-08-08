package com.example.meet_up.presentation.event.select_room

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.meet_up.MainApplication
import com.example.meet_up.R
import com.example.meet_up.databinding.FragmentSelectRoomBinding
import com.example.meet_up.domain.models.RoomModel
import com.example.meet_up.presentation.event.EventConfigViewModel
import com.example.meet_up.presentation.room.adapter.RoomListAdapter
import com.example.meet_up.tools.launchWhenCreated
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class SelectRoomFragment : Fragment(R.layout.fragment_select_room) {

    @Inject
    lateinit var selectRoomViewModelFactory: SelectRoomViewModel.SelectRoomViewModelFactory
    private val viewModel by viewModels<SelectRoomViewModel> { selectRoomViewModelFactory }

    private val eventConfigViewModel by navGraphViewModels<EventConfigViewModel>(R.id.manage_event_graph)

    private val roomListAdapter = RoomListAdapter(::onRoomItemClick)

    private val navController by lazy { requireActivity().findNavController(R.id.menu_container) }

    private val binding by viewBinding<FragmentSelectRoomBinding>()

    override fun onAttach(context: Context) {
        MainApplication.INSTANCE.mainComponent.inflate(this)

        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeViews()
        observeModels()
    }

    private fun initializeViews() {
        setupRecyclerView()

        binding.toolbar.setNavigationOnClickListener {
            navController.navigateUp()
        }
    }

    private fun observeModels() {
        viewModel.roomListFlow.onEach { list ->
            with(binding) {
                if ((list.isEmpty() && roomListViewSwitcher.currentView.id == roomListRecyclerView.id) ||
                    (list.isNotEmpty() && roomListViewSwitcher.currentView.id == textViewEmpty.id)
                ) {
                    roomListViewSwitcher.showNext()
                }

                roomListAdapter.submitList(list)
            }
        }.launchWhenCreated(viewLifecycleOwner)
    }

    private fun setupRecyclerView() {
        val linearLayoutManager = LinearLayoutManager(context)
        val dividerItemDecoration = DividerItemDecoration(
            context,
            linearLayoutManager.orientation,
        )

        binding.roomListRecyclerView.apply {
            layoutManager = linearLayoutManager
            adapter = roomListAdapter

            addItemDecoration(dividerItemDecoration)
        }
    }

    private fun onRoomItemClick(roomModel: RoomModel) {
        eventConfigViewModel.pushRoom(roomModel)
        navController.navigateUp()
    }
}