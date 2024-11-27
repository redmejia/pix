package com.binarystack01.pix.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.binarystack01.pix.data.local.room.dao.PhotoDao
import com.binarystack01.pix.data.local.room.dao.VisionDao
import com.binarystack01.pix.data.local.room.entities.Photo

@Database(
    entities = [Photo::class],
    version = DB_VERSION,
)

abstract class AppDatabase : RoomDatabase() {
    abstract fun photoDao(): PhotoDao
    abstract fun visionDao(): VisionDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    DB_NAME
                ).fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }

    }
}

const val DB_VERSION = 3
const val DB_NAME = "pix_db"