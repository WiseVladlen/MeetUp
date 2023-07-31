package com.example.meet_up.presentation.room.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.meet_up.domain.models.RoomModel

class RoomListAdapter(
    private val listener: RoomItemViewHolder.RoomItemListener,
) : ListAdapter<RoomModel, RoomItemViewHolder>(
    RoomItemDiffCallback
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomItemViewHolder {
        return RoomItemViewHolder.create(parent)
    }

    override fun onBindViewHolder(viewHolder: RoomItemViewHolder, position: Int) {
        viewHolder.bind(getItem(position), listener)
    }
}