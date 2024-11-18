package com.binarystack01.pix.presentation.viewmodel.captureviewmodel

import android.graphics.Bitmap

data class Photo(
    val id: String = "",
    val image: Bitmap,
)

data class ImageState(
    var photos: List<Photo> = emptyList(),
    var photo: Photo? = null,
)
