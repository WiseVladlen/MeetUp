package com.example.meet_up.presentation.room

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.meet_up.MainApplication
import com.example.meet_up.R
import com.example.meet_up.databinding.FragmentRoomListBinding
import com.example.meet_up.domain.models.RoomModel
import com.example.meet_up.presentation.room.adapter.RoomListAdapter
import com.example.meet_up.tools.launchWhenStarted
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class RoomListFragment: Fragment(R.layout.fragment_room_list) {

    @Inject
    lateinit var roomListViewModelFactory: RoomListViewModel.RoomListViewModelFactory
    private val viewModel by viewModels<RoomListViewModel> { roomListViewModelFactory }

    private val currentRoomViewModel by navGraphViewModels<CurrentRoomViewModel>(R.id.menu_nav_graph)

    private val binding by viewBinding<FragmentRoomListBinding>()

    private val navController by lazy { requireActivity().findNavController(R.id.menu_container) }

    private val roomListAdapter = RoomListAdapter(::onRoomItemClick)

    override fun onAttach(context: Context) {
        MainApplication.INSTANCE.mainComponent.inflate(this)

        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeView()
        observeModels()
    }

    private fun initializeView() {
        with(binding) {
            setupRecyclerView()

            addRoomButton.setOnClickListener {
                navController.navigate(RoomListFragmentDirections.actionNavigationRoomListFragmentToAddRoomFragment())
            }
        }
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

    private fun observeModels() {
        viewModel.roomListFlow.onEach { list ->
            with(binding) {
                if (list.isEmpty()) {
                    if (viewSwitcher.currentView.id == roomListRecyclerView.id) {
                        viewSwitcher.showNext()
                    }
                } else if (viewSwitcher.currentView.id == textViewEmpty.id) {
                    roomListAdapter.submitList(list)

                    viewSwitcher.showNext()
                }
            }
        }.launchWhenStarted(lifecycleScope)
    }

    private fun onRoomItemClick(roomModel: RoomModel) {
        currentRoomViewModel.setRoom(roomModel)

        navController.navigate(RoomListFragmentDirections.actionNavigationRoomListFragmentToEditRoomFragment())
    }
}