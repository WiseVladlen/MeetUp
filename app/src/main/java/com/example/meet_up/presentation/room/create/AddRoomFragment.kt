package com.example.meet_up.presentation.room.create

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.meet_up.MainApplication
import com.example.meet_up.R
import com.example.meet_up.databinding.FragmentAddRoomBinding
import javax.inject.Inject

class AddRoomFragment : Fragment(R.layout.fragment_add_room) {

    @Inject
    lateinit var addRoomViewModelFactory: AddRoomViewModel.AddRoomViewModelFactory
    private val viewModel by viewModels<AddRoomViewModel> { addRoomViewModelFactory }

    private val navController by lazy { requireActivity().findNavController(R.id.menu_container) }

    private val binding by viewBinding<FragmentAddRoomBinding>()

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
            toolbar.setNavigationOnClickListener { navController.navigateUp() }

            textViewDone.setOnClickListener {
                viewModel.addRoom(editTextName.text.toString()) {
                    navController.navigateUp()
                }
            }
        }
    }
}