package com.example.meet_up.presentation.room.edit

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.navGraphViewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.meet_up.MainApplication
import com.example.meet_up.R
import com.example.meet_up.databinding.FragmentEditRoomBinding
import com.example.meet_up.presentation.room.CurrentRoomViewModel
import javax.inject.Inject

class EditRoomFragment : Fragment(R.layout.fragment_edit_room) {

    @Inject
    lateinit var editRoomViewModelFactory: EditRoomViewModel.EditRoomViewModelFactory
    private val viewModel by viewModels<EditRoomViewModel> { editRoomViewModelFactory }

    private val navController by lazy { requireActivity().findNavController(R.id.menu_container) }

    private val currentRoomViewModel by navGraphViewModels<CurrentRoomViewModel>(R.id.menu_nav_graph)

    private val binding by viewBinding<FragmentEditRoomBinding>()

    override fun onAttach(context: Context) {
        MainApplication.INSTANCE.mainComponent.inflate(this)

        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeViews()
    }

    private fun initializeViews() {
        with(binding) {

            editTextName.setText(currentRoomViewModel.currentRoom.title)

            toolbar.setNavigationOnClickListener { navController.navigateUp() }

            textViewDone.setOnClickListener {
                viewModel.updateRoom(
                    oldRoomModel = currentRoomViewModel.currentRoom,
                    name = editTextName.text.toString()
                ) {
                    navController.navigateUp()
                }
            }

            deleteButton.setOnClickListener {
                viewModel.deleteRoom(currentRoomViewModel.currentRoom) {
                    navController.navigateUp()
                }
            }
        }
    }
}