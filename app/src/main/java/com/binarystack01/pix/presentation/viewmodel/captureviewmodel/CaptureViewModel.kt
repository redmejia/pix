package com.binarystack01.pix.presentation.viewmodel.captureviewmodel

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.camera.core.ImageCapture.OnImageCapturedCallback
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.view.LifecycleCameraController
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.binarystack01.pix.data.local.room.entities.Photo
import com.binarystack01.pix.data.repositories.room.PhotoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.util.UUID

class CaptureViewModel(private val photoRepository: PhotoRepository) : ViewModel() {

    private val _photoState = MutableStateFlow(PhotoState())
    val photoState: StateFlow<PhotoState> = _photoState.asStateFlow()


    private val WIDTH = 400
    private val HEIGHT = 400
    private val DIRECTORY_NAME = "photos"
    private val IMAGE_FORMAT = "jpg"

    private fun generateUUID(): String = UUID.randomUUID().toString()

    init {
        viewModelScope.launch {
            photoRepository.getAllPhotos().collect { photos ->
                _photoState.update { currentState ->
                    currentState.copy(
                        photos = photos
                    )
                }
                Log.d("PHOTOS", ": $photos")
            }
        }
    }

    private suspend fun savePhoto(context: Context, bitmap: Bitmap, fileName: String): String {

        val photsDir = File(context.filesDir, DIRECTORY_NAME)

        if (!photsDir.exists()) {
            photsDir.mkdir()
        }

        val photo = Bitmap.createScaledBitmap(bitmap, WIDTH, HEIGHT, true)
        val photoFile = File(photsDir, "$fileName.$IMAGE_FORMAT")


        withContext(Dispatchers.IO) {
            FileOutputStream(photoFile).use { outputStream ->
                photo.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            }
        }

        return photoFile.absolutePath

    }

    private suspend fun loadImage(filePath: String): Bitmap {
        val options = BitmapFactory.Options().apply {
            inJustDecodeBounds = true
            BitmapFactory.decodeFile(filePath, this)
            inSampleSize = calculateInSampleSize(this, WIDTH, HEIGHT)
            inJustDecodeBounds = false
        }
        return BitmapFactory.decodeFile(filePath, options)
    }

    private fun calculateInSampleSize(
        options: BitmapFactory.Options,
        reqWidth: Int,
        reqHeight: Int,
    ): Int {
        val (height: Int, width: Int) = options.outHeight to options.outWidth
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {
            val halfHeight: Int = height / 2
            val halfWidth: Int = width / 2
            while ((halfHeight / inSampleSize) >= reqHeight &&
                (halfWidth / inSampleSize) >= reqWidth
            ) {
                inSampleSize *= 2
            }
        }
        return inSampleSize
    }

    // this will be replace
    fun loadPhoto(fileName: String) {
        viewModelScope.launch {
            val filePath = photoRepository.getImage(fileName)
            val photo = loadImage(filePath)
            _photoState.value = _photoState.value.copy(photo = photo)
        }
    }

    fun capturePicture(context: Context, cameraController: LifecycleCameraController) {
        cameraController.takePicture(
            ContextCompat.getMainExecutor(context),
            object : OnImageCapturedCallback() {
                override fun onCaptureSuccess(image: ImageProxy) {
                    super.onCaptureSuccess(image)
                    viewModelScope.launch {
                        val photoId = generateUUID()
                        val path = savePhoto(context, bitmap = image.toBitmap(), photoId)
                        photoRepository.insert(
                            photo = Photo(
                                fileName = "${photoId}",
                                path = path,
                            )
                        )
                    }
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