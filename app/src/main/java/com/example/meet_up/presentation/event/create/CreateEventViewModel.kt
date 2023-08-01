package com.example.meet_up.presentation.event.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.meet_up.data.local.UserStorage
import com.example.meet_up.domain.models.EventModel
import com.example.meet_up.domain.models.RoomModel
import com.example.meet_up.domain.models.UserModel
import com.example.meet_up.domain.interactors.CreateEventInteractor
import com.example.meet_up.domain.interactors.ValidateEventInteractor
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
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject
import javax.inject.Provider

class CreateEventViewModel(
    private val createEventInteractor: CreateEventInteractor,
    private val validateEventInteractor: ValidateEventInteractor,
) : ViewModel() {

    private val createJob: CompletableJob = SupervisorJob()

    private val _onErrorFlow = MutableSharedFlow<String>(0, 1, BufferOverflow.DROP_OLDEST)
    private val _onSuccessFlow = MutableSharedFlow<Unit>(0, 1, BufferOverflow.DROP_OLDEST)

    val onErrorFlow = _onErrorFlow.asSharedFlow()
    val onSuccessFlow = _onSuccessFlow.asSharedFlow()

    val startDate: Calendar = Calendar.getInstance()
    val endDate: Calendar = Calendar.getInstance().apply {
        set(Calendar.MINUTE, get(Calendar.MINUTE) + MIN_EVENT_DURATION)
    }

    fun create(
        title: String,
        users: List<UserModel>,
        roomModel: RoomModel?,
        isAllDay: Boolean,
    ) {
        viewModelScope.launch(Dispatchers.IO + createJob) {
            roomModel?.let {
                val event = EventModel(
                    title = title,
                    startDate = (if (isAllDay) startDate.getStartOfDayCalendar() else startDate).time,
                    endDate = (if (isAllDay) startDate.getEndOfDayCalendar() else endDate).time,
                    users = users,
                    room = roomModel,
                    organizer = UserStorage.user.login,
                )

                validateEventInteractor.invoke(event)
                    .onFailure { _onErrorFlow.emit(it.message.toString()) }
                    .onSuccess {
                        createEventInteractor.invoke(event)
                            .onFailure { _onErrorFlow.emit(it.message.toString()) }
                            .onSuccess {  _onSuccessFlow.emit(Unit) }
                    }
            } ?: _onErrorFlow.emit(NO_ROOM_SELECTED)
        }
    }

    override fun onCleared() {
        super.onCleared()
        createJob.cancel()
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

    class CreateEventViewModelFactory @Inject constructor(
        private val createEventInteractor: Provider<CreateEventInteractor>,
        private val validateEventInteractor: Provider<ValidateEventInteractor>,
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return CreateEventViewModel(
                createEventInteractor.get(),
                validateEventInteractor.get(),
            ) as T
        }
    }
}