package com.example.meet_up.di

import com.example.meet_up.data.remote.repositories.RemoteEventRepositoryImpl
import com.example.meet_up.data.repositories.EventRepositoryImpl
import com.example.meet_up.data.repositories.RoomRepositoryImpl
import com.example.meet_up.data.repositories.UserRepositoryImpl
import com.example.meet_up.domain.repositories.EventRepository
import com.example.meet_up.domain.repositories.RemoteEventRepository
import com.example.meet_up.domain.repositories.RoomRepository
import com.example.meet_up.domain.repositories.UserRepository
import dagger.Binds
import dagger.Module

@Module
abstract class BindingModule {
    @Binds
    abstract fun bindUserRepository(userRepositoryImpl: UserRepositoryImpl): UserRepository
    @Binds
    abstract fun bindEventRepository(eventRepositoryImpl: EventRepositoryImpl): EventRepository
    @Binds
    abstract fun bindRoomRepository(roomRepositoryImpl: RoomRepositoryImpl): RoomRepository
    @Binds
    abstract fun bindRemoteEventRepository(remoteEventRepositoryImpl: RemoteEventRepositoryImpl): RemoteEventRepository
}