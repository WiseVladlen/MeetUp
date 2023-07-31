package com.example.meet_up.presentation.event.manage_participant_list.adapter

import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter

class PeopleListDelegationAdapter(
    participantLayoutListener: ParticipantLayoutListener,
    nonParticipantLayoutListener: NonParticipantLayoutListener,
) : AsyncListDifferDelegationAdapter<PeopleListItem>(
    PeopleListItemDiffCallback,
    participantAdapterDelegate(participantLayoutListener),
    nonParticipantAdapterDelegate(nonParticipantLayoutListener),
)