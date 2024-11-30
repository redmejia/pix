package com.binarystack01.pix.data.local.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.binarystack01.pix.data.local.room.entities.Vision
import kotlinx.coroutines.flow.Flow

@Dao
interface VisionDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(vision: Vision)

    @Query("SELECT * FROM vision_text")
    fun getAll(): Flow<List<Vision>>

    @Query("SELECT * FROM vision_text WHERE id = :id")
    suspend fun getRecord(id: Long): Vision


    // TODO: Update

    @Delete
    suspend fun delete(vision: Vision)

}