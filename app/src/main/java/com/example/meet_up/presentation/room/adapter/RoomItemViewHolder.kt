package com.example.meet_up.presentation.room.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.meet_up.R
import com.example.meet_up.databinding.RoomCardLayoutBinding
import com.example.meet_up.domain.models.RoomModel

class RoomItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val binding by viewBinding<RoomCardLayoutBinding>()

    fun bind(roomModel: RoomModel, listener: RoomItemListener) {
        with(binding.root) {
            text = roomModel.title

            setOnClickListener { listener.onClick(roomModel) }
        }
    }

    companion object {
        fun create(parent: ViewGroup): RoomItemViewHolder {
            return RoomItemViewHolder(
                LayoutInflater
                    .from(parent.context)
                    .inflate(R.layout.room_card_layout, parent, false)
            )
        }
    }

    @FunctionalInterface
    fun interface RoomItemListener {
        fun onClick(roomModel: RoomModel)
    }
}