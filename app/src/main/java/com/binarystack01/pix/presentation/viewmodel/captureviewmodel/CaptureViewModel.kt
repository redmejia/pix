package com.binarystack01.pix.presentation.viewmodel.captureviewmodel

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.camera.core.ImageCapture.OnImageCapturedCallback
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.view.LifecycleCameraController
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.UUID

class CaptureViewModel : ViewModel() {

    private val _photoState = MutableStateFlow(PhotoState())
    val photoState: StateFlow<PhotoState> = _photoState.asStateFlow()

    private fun generateUUID(): String = UUID.randomUUID().toString()

    private fun addPhoto(photo: Bitmap) {
        val id = generateUUID()
        _photoState.update { currentState ->
            currentState.copy(
                photos = currentState.photos + Photo(id = id, photo = photo)
            )
        }
    }

    fun loadPhoto(id: String) {
        val photo = _photoState.value.photos.find { image -> image.id == id }
        _photoState.value = _photoState.value.copy(photo = photo)
    }

    fun capturePicture(context: Context, cameraController: LifecycleCameraController) {
        cameraController.takePicture(
            ContextCompat.getMainExecutor(context),
            object : OnImageCapturedCallback() {
                override fun onCaptureSuccess(image: ImageProxy) {
                    super.onCaptureSuccess(image)
                    addPhoto(image.toBitmap())
                    image.close()
                }

                override fun onError(exception: ImageCaptureException) {
                    super.onError(exception)
                    Log.e("ERROR", "onError: Capture error  ", exception)
                }
            }
        )
    }

}