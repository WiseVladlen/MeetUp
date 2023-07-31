package com.example.meet_up.presentation.event.manage_participant_list.adapter

import com.example.meet_up.databinding.PeopleCardLayoutBinding
import com.example.meet_up.tools.show
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding

fun participantAdapterDelegate(listener: ParticipantLayoutListener) = adapterDelegateViewBinding<PeopleListItem.ParticipantItem, PeopleListItem, PeopleCardLayoutBinding>(
    { layoutInflater, parent -> PeopleCardLayoutBinding.inflate(layoutInflater, parent, false) }
) {
    bind {
        binding.apply {
            textViewTitle.text = item.userModel.login

            buttonDelete.apply {
                show()

                setOnClickListener { listener.onDeleteClick(item) }
            }
        }
    }
}

fun nonParticipantAdapterDelegate(listener: NonParticipantLayoutListener) = adapterDelegateViewBinding<PeopleListItem.NonParticipantItem, PeopleListItem, PeopleCardLayoutBinding>(
    { layoutInflater, parent -> PeopleCardLayoutBinding.inflate(layoutInflater, parent, false) }
) {
    bind {
        binding.apply {
            textViewTitle.text = item.userModel.login

            root.setOnClickListener { listener.onLayoutClick(item) }
        }
    }
}

@FunctionalInterface
fun interface ParticipantLayoutListener {
    fun onDeleteClick(item: PeopleListItem.ParticipantItem)
}

@FunctionalInterface
fun interface NonParticipantLayoutListener {
    fun onLayoutClick(item: PeopleListItem.NonParticipantItem)
}
