package com.binarystack01.pix.data.local.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "vision_text")
data class Vision(
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,
    @ColumnInfo(name = "title")
    val title: String = "",
    @ColumnInfo(name = "text")
    val text: String = "",
    @ColumnInfo(name = "created_at")
    val createdAt: String = "",
)
