package com.binarystack01.pix.presentation.viewmodel.captureviewmodel

import android.graphics.Bitmap

data class Photo(
    val id: String = "",
    val photo: Bitmap,
)

data class PhotoState(
    val photos: List<Photo> = emptyList(),
    val photo: Photo? = null,
)
