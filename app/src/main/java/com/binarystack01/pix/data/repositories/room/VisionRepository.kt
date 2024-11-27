package com.binarystack01.pix.data.repositories.room

import com.binarystack01.pix.data.local.room.dao.VisionDao
import com.binarystack01.pix.data.local.room.entities.Vision
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class VisionRepository(private val visionDao: VisionDao) {

    suspend fun insert(vision: Vision) = withContext(Dispatchers.IO) {
        visionDao.insert(vision)
    }

    fun getAll(): Flow<List<Vision>> = visionDao.getAll()

    suspend fun getRecord(id: Long): Vision = visionDao.getRecord(id)

    suspend fun delete(vision: Vision) = withContext(Dispatchers.IO) {
        visionDao.delete(vision)
    }

}