package com.example.meet_up.data.local

import android.content.ContentValues
import android.content.Context
import androidx.room.Database
import androidx.room.OnConflictStrategy
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.meet_up.data.local.dao.EventDao
import com.example.meet_up.data.local.dao.RoomDao
import com.example.meet_up.data.local.dao.UserDao
import com.example.meet_up.data.local.entities.Event
import com.example.meet_up.data.local.entities.EventUser
import com.example.meet_up.data.local.entities.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [User::class, Event::class, EventUser::class, com.example.meet_up.data.local.entities.Room::class],
    version = DataBase.VERSION
)
@TypeConverters(Converters::class)
abstract class DataBase : RoomDatabase() {

    abstract val userDao: UserDao
    abstract val eventDao: EventDao
    abstract val roomDao: RoomDao

    companion object {
        const val VERSION = 1

        fun getDatabase(appContext: Context): DataBase {
            synchronized(this) {
                return Room.databaseBuilder(
                    appContext,
                    DataBase::class.java,
                    DataBase::class.java.simpleName
                )
                    .addCallback(PreloadingCallback())
                    .build()
            }
        }
    }

    private class PreloadingCallback : Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)

            CoroutineScope(Dispatchers.IO).launch {

                db.run {
                    beginTransaction()

                    preliminaryUserList.forEach { user ->
                        insert(User.TABLE_NAME, OnConflictStrategy.REPLACE, ContentValues().apply {
                            put(User.LOGIN_COLUMN_NAME, user.login)
                            put(User.PASSWORD_COLUMN_NAME, user.password)
                        })
                    }

                    setTransactionSuccessful()
                    endTransaction()
                }
            }
        }
    }
}