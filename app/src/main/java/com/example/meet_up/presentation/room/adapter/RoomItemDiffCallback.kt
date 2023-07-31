package com.example.meet_up.presentation.room.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.meet_up.domain.models.RoomModel

object RoomItemDiffCallback : DiffUtil.ItemCallback<RoomModel>() {

    override fun areItemsTheSame(oldItem: RoomModel, newItem: RoomModel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: RoomModel, newItem: RoomModel): Boolean {
        return oldItem == newItem
    }
}