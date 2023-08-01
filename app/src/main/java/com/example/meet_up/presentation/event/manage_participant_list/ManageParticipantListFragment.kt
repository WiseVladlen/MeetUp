package com.example.meet_up.presentation.event.manage_participant_list

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.meet_up.MainApplication
import com.example.meet_up.R
import com.example.meet_up.databinding.FragmentManageParticipantListBinding
import com.example.meet_up.presentation.event.EventConfigViewModel
import com.example.meet_up.presentation.event.manage_participant_list.adapter.NonParticipantLayoutListener
import com.example.meet_up.presentation.event.manage_participant_list.adapter.ParticipantLayoutListener
import com.example.meet_up.presentation.event.manage_participant_list.adapter.PeopleListDelegationAdapter
import com.example.meet_up.presentation.event.manage_participant_list.adapter.PeopleListItem
import com.example.meet_up.tools.hide
import com.example.meet_up.tools.launchWhenStarted
import com.example.meet_up.tools.show
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class ManageParticipantListFragment : Fragment(R.layout.fragment_manage_participant_list) {

    @Inject
    lateinit var manageParticipantListViewModelFactory: ManageParticipantListViewModel.ManageParticipantListViewModelFactory
    private val viewModel by viewModels<ManageParticipantListViewModel> { manageParticipantListViewModelFactory }

    private val eventConfigViewModel by navGraphViewModels<EventConfigViewModel>(R.id.menu_nav_graph)

    private val participantLayoutListener = ParticipantLayoutListener {
        viewModel.removeParticipant(it.userModel)
    }

    private val nonParticipantLayoutListener = NonParticipantLayoutListener {
        viewModel.addParticipant(it.userModel)

        binding.editTextLogin.setText(String())
    }

    private val peopleListAdapter = PeopleListDelegationAdapter(
        participantLayoutListener,
        nonParticipantLayoutListener
    )

    private val navController by lazy { requireActivity().findNavController(R.id.menu_container) }

    private val binding by viewBinding<FragmentManageParticipantListBinding>()

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

        with(binding) {
            toolbar.setNavigationOnClickListener {
                navController.navigateUp()
            }

            editTextLogin.doOnTextChanged { text, _, _, count ->
                viewModel.searchQuery.value = text.toString()

                if (count > 0) {
                    textViewDone.hide()
                } else {
                    textViewDone.show()
                }
            }

            textViewDone.setOnClickListener {
                eventConfigViewModel.pushParticipantList(viewModel.temporaryParticipantList)
                navController.navigateUp()
            }
        }
    }

    private fun observeModels() {
        viewModel.userListFlow.onEach { list ->
            peopleListAdapter.items = list.map { PeopleListItem.NonParticipantItem(it) }
        }.launchWhenStarted(lifecycleScope)

        viewModel.participantListFlow.onEach { list ->
            peopleListAdapter.items = list.map { PeopleListItem.ParticipantItem(it) }
        }.launchWhenStarted(lifecycleScope)
    }

    private fun setupRecyclerView() {
        binding.participantListRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = peopleListAdapter
        }

        eventConfigViewModel.temporaryParticipantList.let { list ->
            viewModel.initParticipants(list)
        }
    }
}