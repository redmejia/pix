package com.binarystack01.pix.presentation.viewmodel.visionviewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.binarystack01.pix.data.local.room.entities.Vision
import com.binarystack01.pix.data.repositories.room.VisionRepository
import com.binarystack01.pix.domain.usecases.time.Time
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class VisionViewModel(private val visionRepository: VisionRepository) : ViewModel() {

    private val _visionState = MutableStateFlow(VisionState())
    val visionState: StateFlow<VisionState> = _visionState.asStateFlow()

    private val time = Time()

    init {
        viewModelScope.launch {
            visionRepository.getAll().collect { vision ->
                _visionState.update { currentState ->
                    currentState.copy(data = vision)
                }
            }
        }
    }

    private fun timeNow(): String = time.timeFormater(format = TIME_FORMAT)

    fun saveVisionText(title: String, text: String) {

        viewModelScope.launch {
            visionRepository.insert(
                vision = Vision(
                    title = title,
                    text = text,
                    createdAt = timeNow()
                )
            )
        }
    }

    companion object {
        private const val TIME_FORMAT = "MMM dd, yyyy HH:mm a"
    }

}