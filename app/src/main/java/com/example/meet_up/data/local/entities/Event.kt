package com.example.meet_up.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.meet_up.data.local.entities.Event.Companion.TABLE_NAME
import java.util.Date
import java.util.UUID

@Entity(tableName = TABLE_NAME, foreignKeys = [ForeignKey(
    entity = Room::class,
    parentColumns = [Room.ID_COLUMN_NAME],
    childColumns = [Event.ROOM_ID_COLUMN_NAME],
    onDelete = ForeignKey.CASCADE
)])
data class Event(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = ID_COLUMN_NAME)
    val id: String = UUID.randomUUID().toString(),
    @ColumnInfo(name = TITLE_COLUMN_NAME)
    val title: String,
    @ColumnInfo(name = START_DATE_COLUMN_NAME)
    val startDate: Date,
    @ColumnInfo(name = END_DATE_COLUMN_NAME)
    val endDate: Date,
    @ColumnInfo(name = ROOM_ID_COLUMN_NAME)
    val roomId: Int,
    @ColumnInfo(name = ORGANIZER_COLUMN_NAME)
    val organizer: String
) {
    companion object {
        const val TABLE_NAME = "event"
        const val ID_COLUMN_NAME = "event_id"
        const val TITLE_COLUMN_NAME = "title"
        const val START_DATE_COLUMN_NAME = "start_date"
        const val END_DATE_COLUMN_NAME = "end_date"
        const val ROOM_ID_COLUMN_NAME = "room_id"
        const val ORGANIZER_COLUMN_NAME = "organizer"
    }
}
