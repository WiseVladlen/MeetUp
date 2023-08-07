package com.example.meet_up.presentation.room.manage

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.meet_up.MainApplication
import com.example.meet_up.R
import com.example.meet_up.databinding.FragmentEditRoomBinding
import com.example.meet_up.domain.models.RoomModel
import com.example.meet_up.tools.launchWhenCreated
import com.example.meet_up.tools.show
import com.example.meet_up.tools.showKeyboard
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onSubscription
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.properties.Delegates

class ManageRoomFragment : Fragment(R.layout.fragment_edit_room) {

    @Inject
    lateinit var manageRoomViewModelFactory: ManageRoomViewModel.ManageRoomViewModelFactory
    private val viewModel by viewModels<ManageRoomViewModel> { manageRoomViewModelFactory }

    private val navController by lazy { requireActivity().findNavController(R.id.menu_container) }

    private val binding by viewBinding<FragmentEditRoomBinding>()

    private val args: ManageRoomFragmentArgs by navArgs()

    private var roomId by Delegates.notNull<Int>()

    override fun onAttach(context: Context) {
        MainApplication.INSTANCE.mainComponent.inflate(this)

        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        roomId = args.roomId
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeViews()
        observeModel()
    }

    private fun initializeViews() {
        with(binding) {
            toolbar.setNavigationOnClickListener { navController.navigateUp() }

            editTextName.showKeyboard(requireActivity().window)

            textViewDone.setOnClickListener {
                lifecycleScope.launch {
                    viewModel.put(roomId, editTextName.text.toString())
                }
            }

            if (roomId != RoomModel.DEFAULT_ID) {
                deleteButton.apply {
                    show()

                    setOnClickListener { viewModel.delete(roomId) }
                }
                toolbar.title = getString(R.string.manage_room_edit_room_title)
            } else {
                toolbar.title = getString(R.string.manage_room_add_room_title)
            }
        }
    }

    private fun observeModel() {
        viewModel.roomFlow
            .onSubscription { viewModel.requestRoom(roomId) }
            .onEach { result ->
                result.onSuccess { room ->
                    binding.editTextName.apply {
                        setText(room.title)
                        setSelection(room.title.length)
                    }
                }
            }.launchWhenCreated(viewLifecycleOwner)

        viewModel.onSuccessFlow.onEach {
            navController.navigateUp()
        }.launchWhenCreated(viewLifecycleOwner)

        viewModel.onErrorFlow.onEach { message ->
            Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
        }.launchWhenCreated(viewLifecycleOwner)
    }
}