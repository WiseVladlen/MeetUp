package com.example.meet_up.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.meet_up.data.local.entities.Room.Companion.TABLE_NAME

@Entity(tableName = TABLE_NAME)
data class Room(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = ID_COLUMN_NAME)
    val id: Int = DEFAULT_ID,
    @ColumnInfo(name = TITLE_COLUMN_NAME)
    val title: String
) {
    companion object {
        const val TABLE_NAME = "room"
        const val ID_COLUMN_NAME = "id"
        const val TITLE_COLUMN_NAME = "title"
        private const val DEFAULT_ID = 0
    }
}
