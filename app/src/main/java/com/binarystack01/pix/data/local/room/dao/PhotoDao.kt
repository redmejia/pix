package com.binarystack01.pix.data.local.room.dao

import androidx.room.Dao
import androidx.room.Delete
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

    @Query("SELECT photo_path FROM user_photos WHERE file_name = :fileName")
    suspend fun getPhotoPath(fileName: String): String

    @Query("SELECT thumbnail_path FROM user_photos WHERE file_name = :fileName")
    suspend fun getThumbnailPath(fileName: String): String

    @Delete
    suspend fun deleteImage(photo: Photo)

}