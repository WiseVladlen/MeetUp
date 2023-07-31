package com.example.meet_up.presentation.event.manage_participant_list.adapter

import com.example.meet_up.domain.models.UserModel

sealed class PeopleListItem {
    class ParticipantItem(val userModel: UserModel): PeopleListItem()
    class NonParticipantItem(val userModel: UserModel): PeopleListItem()
}