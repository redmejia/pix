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

    fun getText(id: Long) {
        viewModelScope.launch {
            _visionState.update { currentState ->
                currentState.copy(text = visionRepository.getRecord(id))
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

    fun deleteVisionTextRecord(vision: Vision) {
        viewModelScope.launch {
            visionRepository.delete(vision)
        }
    }

    fun changeMode(navigation: () -> Unit) {
        _visionState.update { currentState ->
            currentState.copy(readerMode = true)
        }
        navigation()
    }

    fun resetReaderMode() {
        _visionState.update { currentState ->
            currentState.copy(readerMode = false)
        }
    }

    fun isReadMode(): Boolean = _visionState.value.readerMode

    companion object {
        private const val TIME_FORMAT = "MMM dd, yyyy HH:mm a"
    }

}