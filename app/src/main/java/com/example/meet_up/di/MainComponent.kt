package com.example.meet_up.di

import android.content.Context
import com.example.meet_up.presentation.authorization.AuthorizationFragment
import com.example.meet_up.presentation.bottom_navigation.BottomNavigationFragment
import com.example.meet_up.presentation.calendar.CalendarFragment
import com.example.meet_up.presentation.event.EventListFragment
import com.example.meet_up.presentation.event.manage.ManageEventFragment
import com.example.meet_up.presentation.event.manage_participant_list.ManageParticipantListFragment
import com.example.meet_up.presentation.event.select_room.SelectRoomFragment
import com.example.meet_up.presentation.room.RoomListFragment
import com.example.meet_up.presentation.room.manage.ManageRoomFragment
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [MainModule::class])
interface MainComponent {
    fun inflate(fragment: AuthorizationFragment)
    fun inflate(fragment: BottomNavigationFragment)
    fun inflate(fragment: CalendarFragment)
    fun inflate(fragment: RoomListFragment)
    fun inflate(fragment: ManageRoomFragment)
    fun inflate(fragment: ManageParticipantListFragment)
    fun inflate(fragment: SelectRoomFragment)
    fun inflate(fragment: EventListFragment)
    fun inflate(fragment: ManageEventFragment)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun addContext(context: Context): Builder
        fun build(): MainComponent
    }
}