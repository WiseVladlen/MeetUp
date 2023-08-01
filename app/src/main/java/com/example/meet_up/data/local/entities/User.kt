package com.example.meet_up.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = User.TABLE_NAME)
data class User(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = LOGIN_COLUMN_NAME)
    val login: String,
    @ColumnInfo(name = PASSWORD_COLUMN_NAME)
    val password: String,
) {
    companion object {
        const val TABLE_NAME = "user"
        const val LOGIN_COLUMN_NAME = "user_login"
        const val PASSWORD_COLUMN_NAME = "password"
    }
}
