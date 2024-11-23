package com.binarystack01.pix.data.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.binarystack01.pix.data.local.room.entities.Photo
import kotlinx.coroutines.flow.Flow

@Dao
interface PhotoDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(photo: Photo)

    @Query("SELECT * FROM user_photos")
    fun getAllPhotos(): Flow<List<Photo>>

}