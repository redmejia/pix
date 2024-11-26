package com.binarystack01.pix.presentation.viewmodel.visionviewmodel

data class VisionData(
    val title: String,
    val text: String,
    val createdAt: String,
)


data class VisionState(
    val data: List<VisionData> = emptyList(),
)
