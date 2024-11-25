package com.binarystack01.pix.data.local.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_photos")
data class Photo(
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,
    @ColumnInfo(name = "file_name") // ID File Name
    val fileName: String = "",
    @ColumnInfo(name = "thumbnail_path")
    val thumbnailPath: String = "",
    @ColumnInfo(name = "photo_path")
    val path: String = "",
)
