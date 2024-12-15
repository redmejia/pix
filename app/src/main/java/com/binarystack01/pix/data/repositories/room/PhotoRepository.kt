package com.binarystack01.pix.data.repositories.room

import com.binarystack01.pix.data.local.room.dao.PhotoDao
import com.binarystack01.pix.data.local.room.entities.Photo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class PhotoRepository(private val photoDao: PhotoDao) {

    suspend fun insert(photo: Photo) = withContext(Dispatchers.IO) {
        photoDao.insert(photo)
    }


    fun getAllPhotos(): Flow<List<Photo>> {
        return photoDao.getAllPhotos()
    }

    suspend fun getImagePath(fileName: String): String = withContext(Dispatchers.IO) {
        photoDao.getPhotoPath(fileName)
    }

}