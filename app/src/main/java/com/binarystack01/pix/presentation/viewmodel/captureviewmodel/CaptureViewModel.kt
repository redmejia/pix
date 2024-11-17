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
import java.util.UUID

class CaptureViewModel : ViewModel() {

    private val _imageListState = MutableStateFlow<List<ImageState>>(emptyList())
    val imageListState: StateFlow<List<ImageState>> = _imageListState.asStateFlow()

    private fun addPhoto(image: Bitmap) {
        val id = generateUUID()
        val newItem = _imageListState.value.toMutableList()
        newItem.add(ImageState(id = id, image = image))
        _imageListState.value = newItem
    }

    private fun generateUUID(): String = UUID.randomUUID().toString()

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