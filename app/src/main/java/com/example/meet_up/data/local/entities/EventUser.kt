package com.example.meet_up.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.meet_up.data.local.entities.EventUser.Companion.EVENT_ID_COLUMN_NAME
import com.example.meet_up.data.local.entities.EventUser.Companion.TABLE_NAME
import com.example.meet_up.data.local.entities.EventUser.Companion.USER_LOGIN_COLUMN_NAME

@Entity(
    tableName = TABLE_NAME, foreignKeys = [ForeignKey(
        entity = Event::class,
        parentColumns = [Event.ID_COLUMN_NAME],
        childColumns = [EVENT_ID_COLUMN_NAME],
        onDelete = ForeignKey.CASCADE
    ), ForeignKey(
        entity = User::class,
        parentColumns = [User.LOGIN_COLUMN_NAME],
        childColumns = [USER_LOGIN_COLUMN_NAME],
        onDelete = ForeignKey.CASCADE
    )]
)
data class EventUser(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = ID_COLUMN_NAME)
    val id: Int = DEFAULT_ID,
    @ColumnInfo(name = EVENT_ID_COLUMN_NAME)
    val eventId: String,
    @ColumnInfo(name = USER_LOGIN_COLUMN_NAME)
    val userLogin: String
) {
    companion object {
        const val TABLE_NAME = "event_user"
        const val ID_COLUMN_NAME = "id"
        const val EVENT_ID_COLUMN_NAME = "event_id"
        const val USER_LOGIN_COLUMN_NAME = "user_login"
        private const val DEFAULT_ID = 0
    }
}
