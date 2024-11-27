package com.binarystack01.pix.presentation.viewmodel.visionviewmodel

import androidx.lifecycle.ViewModel
import com.binarystack01.pix.data.repositories.room.VisionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class VisionViewModel(private val visionRepository: VisionRepository) : ViewModel() {

    private val _visionState = MutableStateFlow(VisionState())
    val visionState: StateFlow<VisionState> = _visionState.asStateFlow()


    fun loadVisionData() {
        // Not implemented yet
        println(visionState.value)
    }


}