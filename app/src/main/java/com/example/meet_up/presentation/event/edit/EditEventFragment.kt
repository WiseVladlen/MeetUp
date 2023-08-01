package com.example.meet_up.presentation.event.edit

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.meet_up.MainApplication
import com.example.meet_up.R
import com.example.meet_up.databinding.FragmentManageEventBinding
import com.example.meet_up.presentation.event.ManageEventViewModel
import com.example.meet_up.tools.copyFrom
import com.example.meet_up.tools.hide
import com.example.meet_up.tools.isAllDay
import com.example.meet_up.tools.launchWhenCreated
import com.example.meet_up.tools.show
import com.example.meet_up.tools.toFullFormat
import com.example.meet_up.tools.toShortFormat
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import java.time.Duration
import java.util.Calendar
import javax.inject.Inject

class EditEventFragment : Fragment(R.layout.fragment_manage_event) {

    @Inject
    lateinit var editEventViewModelFactory: EditEventViewModel.EditEventViewModelFactory
    private val viewModel by viewModels<EditEventViewModel> { editEventViewModelFactory }

    private val manageEventViewModel by navGraphViewModels<ManageEventViewModel>(R.id.menu_nav_graph)

    private val navController by lazy { requireActivity().findNavController(R.id.menu_container) }

    private val binding by viewBinding<FragmentManageEventBinding>()

    private val args: EditEventFragmentArgs by navArgs()

    private lateinit var eventId: String

    private val onStartDateSetListener = OnDateSetListener { _, year, month, dayOfMonth ->
            binding.startDateTimeCard.textViewDate.text =
                viewModel.updateStartDate(year, month, dayOfMonth)
        }

    private val onStartTimeSetListener = OnTimeSetListener { _, hourOfDay, minute ->
        binding.startDateTimeCard.textViewTime.text =
            viewModel.updateStartDateDayTime(hourOfDay, minute)
    }

    private val onEndDateSetListener = OnDateSetListener { _, year, month, dayOfMonth ->
        binding.endDateTimeCard.textViewDate.text = viewModel.updateEndDate(year, month, dayOfMonth)
    }

    private val onEndTimeSetListener = OnTimeSetListener { _, hourOfDay, minute ->
        binding.endDateTimeCard.textViewTime.text = viewModel.updateEndDateDayTime(hourOfDay, minute)
    }

    override fun onAttach(context: Context) {
        MainApplication.INSTANCE.mainComponent.inflate(this)

        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        eventId = args.eventId
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeViews()
        observeModel()
    }

    private fun observeModel() {
        viewModel.eventFlow
            .onStart { viewModel.requestEvent(eventId) }
            .onEach { event ->
                binding.apply {
                    editTextTitle.setText(event.title)

                    manageEventViewModel.pushRoom(event.room)
                    manageEventViewModel.pushParticipantList(event.users)

                    startDateTimeCard.apply {
                        textViewDate.text = event.startDate.toFullFormat()
                        textViewTime.text = event.startDate.toShortFormat()
                    }

                    endDateTimeCard.apply {
                        textViewDate.text = event.endDate.toFullFormat()
                        textViewTime.text = event.endDate.toShortFormat()
                    }

                    if (Duration.ofMillis(event.endDate.time - event.startDate.time).isAllDay()) {
                        allDayCard.modeSwitch.isChecked = true
                    }

                    viewModel.startDate.copyFrom(Calendar.Builder().setInstant(event.startDate).build())
                    viewModel.endDate.copyFrom(Calendar.Builder().setInstant(event.endDate).build())
                }
        }.launchWhenCreated(viewLifecycleOwner)

        manageEventViewModel.roomFlow.onEach { room ->
            binding.locationCard.root.text = room.title
        }.launchWhenCreated(viewLifecycleOwner)

        viewModel.onErrorFlow.onEach { message ->
            Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
        }.launchWhenCreated(viewLifecycleOwner)

        viewModel.onSuccessFlow.onEach {
            navController.navigateUp()
        }.launchWhenCreated(viewLifecycleOwner)
    }

    private fun initializeViews() {
        with(binding) {
            toolbar.setNavigationOnClickListener { navController.navigateUp() }

            allDayCard.apply {
                root.setOnClickListener {
                    modeSwitch.isChecked = !modeSwitch.isChecked
                }

                modeSwitch.setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked) {
                        startDateTimeCard.textViewTime.hide()
                        endDateTimeCard.textViewTime.hide()
                    } else {
                        startDateTimeCard.textViewTime.show()
                        endDateTimeCard.textViewTime.show()
                    }
                }
            }

            startDateTimeCard.apply {
                textViewDate.apply {
                    text = viewModel.startDate.time.toFullFormat()

                    setOnClickListener {
                        DatePickerDialog(
                            requireContext(),
                            onStartDateSetListener,
                            viewModel.startDate.get(Calendar.YEAR),
                            viewModel.startDate.get(Calendar.MONTH),
                            viewModel.startDate.get(Calendar.DAY_OF_MONTH),
                        ).show()
                    }
                }

                textViewTime.apply {
                    text = viewModel.startDate.time.toShortFormat()

                    setOnClickListener {
                        TimePickerDialog(
                            requireContext(),
                            onStartTimeSetListener,
                            viewModel.startDate.get(Calendar.HOUR_OF_DAY),
                            viewModel.startDate.get(Calendar.MINUTE),
                            true
                        ).show()
                    }
                }
            }

            endDateTimeCard.apply {
                textViewDate.apply {
                    text = viewModel.endDate.time.toFullFormat()

                    setOnClickListener {
                        DatePickerDialog(
                            requireContext(),
                            onEndDateSetListener,
                            viewModel.endDate.get(Calendar.YEAR),
                            viewModel.endDate.get(Calendar.MONTH),
                            viewModel.endDate.get(Calendar.DAY_OF_MONTH),
                        ).show()
                    }
                }

                textViewTime.apply {
                    text = viewModel.endDate.time.toShortFormat()

                    setOnClickListener {
                        TimePickerDialog(
                            requireContext(),
                            onEndTimeSetListener,
                            viewModel.endDate.get(Calendar.HOUR_OF_DAY),
                            viewModel.endDate.get(Calendar.MINUTE),
                            true
                        ).show()
                    }
                }
            }

            participantListCard.root.apply {
                text = getString(R.string.participant_list_title)
                setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_peoples, 0, 0, 0)

                setOnClickListener {
                    navController.navigate(EditEventFragmentDirections.actionEditEventFragmentToManageParticipantListFragment())
                }
            }

            locationCard.root.apply {
                setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_location, 0, 0, 0)

                setOnClickListener {
                    navController.navigate(EditEventFragmentDirections.actionEditEventFragmentToSelectRoomFragment())
                }
            }

            buttonDelete.apply {
                show()

                setOnClickListener { viewModel.delete(eventId) }
            }

            textViewDone.setOnClickListener {
                lifecycleScope.launch {
                    manageEventViewModel.roomFlow.firstOrNull().let { room ->
                        viewModel.update(
                            eventId = eventId,
                            title = editTextTitle.text.toString(),
                            users = manageEventViewModel.temporaryParticipantList,
                            roomModel = room,
                            isAllDay = allDayCard.modeSwitch.isChecked,
                        )
                    }
                }
            }
        }
    }
}