package com.example.meet_up.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.meet_up.data.local.entities.Room
import kotlinx.coroutines.flow.Flow

@Dao
interface RoomDao {

    @Query("SELECT * FROM room WHERE id = :id LIMIT 1")
    suspend fun getRoom(id: Int): Room

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun createRoom(room: Room): Long

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateRoom(room: Room): Int

    @Query("SELECT * FROM room")
    fun getAllRooms(): Flow<List<Room>>

    @Transaction
    @Query("DELETE FROM room WHERE id == :roomId")
    suspend fun deleteRoomById(roomId: Int)
}