package com.example.meet_up.presentation.calendar.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.meet_up.R
import com.example.meet_up.databinding.EventCardLayoutBinding

class EventViewHolder(view: View): ViewHolder(view) {
    private val binding by viewBinding<EventCardLayoutBinding>()

    fun bind(eventDisplay: EventDisplay, onEventClickListener: OnEventClickListener) {
        binding.root.apply {

            text = resources.getString(R.string.event_card_title, eventDisplay.title, eventDisplay.roomTitle)

            setOnClickListener { onEventClickListener(eventDisplay.id) }
        }
    }

    companion object {
        fun create(parent: ViewGroup): EventViewHolder {
            return EventViewHolder(
                LayoutInflater
                    .from(parent.context)
                    .inflate(R.layout.event_card_layout, parent, false)
            )
        }
    }

    @FunctionalInterface
    fun interface OnEventClickListener {
        operator fun invoke(eventId: String)
    }
}