package com.binarystack01.pix.presentation.viewmodel.visionviewmodel

import com.binarystack01.pix.data.local.room.entities.Vision

data class VisionState(
    val data: List<Vision> = emptyList(),
)
