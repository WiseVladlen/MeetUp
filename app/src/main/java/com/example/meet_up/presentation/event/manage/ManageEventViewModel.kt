package com.example.meet_up.presentation.event.manage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.meet_up.data.local.UserStorage
import com.example.meet_up.domain.use_cases.CreateEvent
import com.example.meet_up.domain.models.EventModel
import com.example.meet_up.domain.models.RoomModel
import com.example.meet_up.domain.models.UserModel
import com.example.meet_up.domain.use_cases.DeleteEvent
import com.example.meet_up.domain.use_cases.LoadEvent
import com.example.meet_up.domain.use_cases.UpdateEvent
import com.example.meet_up.domain.use_cases.ValidateEvent
import com.example.meet_up.domain.models.EventModel.Companion.validate
import com.example.meet_up.tools.MIN_EVENT_DURATION
import com.example.meet_up.tools.getEndOfDayCalendar
import com.example.meet_up.tools.getStartOfDayCalendar
import com.example.meet_up.tools.toFullFormat
import com.example.meet_up.tools.toShortFormat
import kotlinx.coroutines.CompletableJob
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject
import javax.inject.Provider

class ManageEventViewModel(
    private val loadEvent: LoadEvent,
    private val updateEvent: UpdateEvent,
    private val createEvent: CreateEvent,
    private val deleteEvent: DeleteEvent,
    private val validateEvent: ValidateEvent,
) : ViewModel() {

    private val putJob: CompletableJob = SupervisorJob()
    private val deleteJob: CompletableJob = SupervisorJob()

    private val _eventFlow = MutableSharedFlow<Result<EventModel>>(0, 1, BufferOverflow.DROP_OLDEST)

    private val _onErrorFlow = MutableSharedFlow<String>(0, 1, BufferOverflow.DROP_OLDEST)
    private val _onSuccessFlow = MutableSharedFlow<Unit>(0, 1, BufferOverflow.DROP_OLDEST)

    val eventFlow = _eventFlow.take(1).shareIn(
        viewModelScope,
        SharingStarted.Lazily
    )

    val onErrorFlow = _onErrorFlow.asSharedFlow()
    val onSuccessFlow = _onSuccessFlow.asSharedFlow()

    val startDate: Calendar = Calendar.getInstance()
    val endDate: Calendar = Calendar.getInstance().apply {
        set(Calendar.MINUTE, get(Calendar.MINUTE) + MIN_EVENT_DURATION)
    }

    fun requestEvent(eventId: String) {
        viewModelScope.launch {
            _eventFlow.emit(loadEvent(eventId))
        }
    }

    fun put(
        eventId: String,
        title: String,
        users: List<UserModel>,
        roomModel: RoomModel?,
        isAllDay: Boolean,
    ) {
        viewModelScope.launch(Dispatchers.IO + putJob) {
            if (roomModel == null) return@launch _onErrorFlow.emit(NO_ROOM_SELECTED)

            val event = EventModel(
                id = eventId.validate(),
                title = title,
                startDate = (if (isAllDay) startDate.getStartOfDayCalendar() else startDate).time,
                endDate = (if (isAllDay) startDate.getEndOfDayCalendar() else endDate).time,
                users = users,
                room = roomModel,
                organizer = UserStorage.user.login,
            )

            validateEvent(event)
                .onFailure { _onErrorFlow.emit(it.message.toString()) }
                .onSuccess {
                    if (event.id == eventId) {
                        updateEvent(event)
                            .onFailure { _onErrorFlow.emit(it.message.toString()) }
                            .onSuccess { _onSuccessFlow.emit(it) }
                    } else {
                        createEvent(event)
                            .onFailure { _onErrorFlow.emit(it.message.toString()) }
                            .onSuccess { _onSuccessFlow.emit(it) }
                    }
                }
        }
    }

    fun delete(eventId: String) {
        viewModelScope.launch(Dispatchers.IO + deleteJob) {
            deleteEvent(eventId)
                .onFailure { _onErrorFlow.emit(it.message.toString()) }
                .onSuccess { _onSuccessFlow.emit(Unit) }
        }
    }

    fun updateStartDate(year: Int, month: Int, dayOfMonth: Int): String {
        startDate.set(year, month, dayOfMonth)
        return startDate.time.toFullFormat()
    }

    fun updateStartDateDayTime(hour: Int, minute: Int): String {
        startDate.apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
        }
        return startDate.time.toShortFormat()
    }

    fun updateEndDate(year: Int, month: Int, dayOfMonth: Int): String {
        endDate.set(year, month, dayOfMonth)
        return endDate.time.toFullFormat()
    }

    fun updateEndDateDayTime(hour: Int, minute: Int): String {
        endDate.apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
        }
        return endDate.time.toShortFormat()
    }

    companion object {
        private const val NO_ROOM_SELECTED = "No room selected"
    }

    class ManageEventViewModelFactory @Inject constructor(
        private val loadEvent: Provider<LoadEvent>,
        private val updateEvent: Provider<UpdateEvent>,
        private val createEvent: Provider<CreateEvent>,
        private val deleteEvent: Provider<DeleteEvent>,
        private val validateEvent: Provider<ValidateEvent>,
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return ManageEventViewModel(
                loadEvent.get(),
                updateEvent.get(),
                createEvent.get(),
                deleteEvent.get(),
                validateEvent.get(),
            ) as T
        }
    }
}