package com.binarystack01.pix.presentation.viewmodel.captureviewmodel

import android.graphics.Bitmap
import com.binarystack01.pix.data.local.room.entities.Photo

data class PhotoState(
    val photos: List<Photo> = emptyList(),
    val photo: Bitmap? = null,
)
