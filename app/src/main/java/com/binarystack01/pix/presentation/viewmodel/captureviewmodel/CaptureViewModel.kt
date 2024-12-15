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

    private suspend fun writeAndSave(file: File, bitmap: Bitmap) {
        withContext(Dispatchers.IO) {
            FileOutputStream(file).use { outputStream ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            }
        }
    }

    private suspend fun readAndDeleteFile(filePath: String): Boolean {
        val file = File(filePath)
        if (file.exists()) {
            file.delete()
            return true
        }
        return false
    }


    private suspend fun savePhoto(context: Context, bitmap: Bitmap, fileName: String): String {

        val photsDir = File(context.filesDir, DIRECTORY_NAME)

        if (!photsDir.exists()) {
            photsDir.mkdir()
        }

        val photoFile = File(photsDir, "$fileName.$IMAGE_FORMAT")

        writeAndSave(photoFile, bitmap)

        return photoFile.absolutePath
    }

    private suspend fun saveThumbnail(context: Context, bitmap: Bitmap, fileName: String): String {

        val thumbnailDir = File(context.filesDir, "thumbnails")

        if (!thumbnailDir.exists()) {
            thumbnailDir.mkdir()
        }

        val photo = Bitmap.createScaledBitmap(bitmap, WIDTH, HEIGHT, true)
        val photoFile = File(thumbnailDir, "$fileName.$IMAGE_FORMAT")

        writeAndSave(photoFile, photo)

        return photoFile.absolutePath

    }

    fun loadPhoto(fileName: String) {
        viewModelScope.launch {
            val filePath = photoRepository.getImagePath(fileName)
            val photo = BitmapFactory.decodeFile(filePath)
            _photoState.value = _photoState.value.copy(photo = photo)
        }
    }

    fun deleteImage(photo: Photo) {
        viewModelScope.launch {

            val thumbnailPath = photoRepository.getThumbnailImagePath(photo.fileName)
            val photPath = photoRepository.getImagePath(photo.fileName)

            val thumbnail = readAndDeleteFile(thumbnailPath)
            val image = readAndDeleteFile(photPath)

            if (thumbnail && image) {
                photoRepository.deleteImage(photo)
                Log.d("IMAGE", "deleteImage: image was deleted")
            }

        }
    }

    fun resetPhotoState() {
        _photoState.value = _photoState.value.copy(photo = null)
    }

    fun capturePicture(context: Context, cameraController: LifecycleCameraController) {
        cameraController.takePicture(
            ContextCompat.getMainExecutor(context),
            object : OnImageCapturedCallback() {
                override fun onCaptureSuccess(image: ImageProxy) {
                    super.onCaptureSuccess(image)
                    viewModelScope.launch {
                        val photoId = generateUUID()
                        val thumbnailPath =
                            saveThumbnail(context, bitmap = image.toBitmap(), photoId)
                        val photoPath = savePhoto(context, bitmap = image.toBitmap(), photoId)
                        photoRepository.insert(
                            photo = Photo(
                                fileName = "${photoId}",
                                thumbnailPath = thumbnailPath,
                                path = photoPath,
                            )
                        )
                        image.close()
                    }
                }

                override fun onError(exception: ImageCaptureException) {
                    super.onError(exception)
                    Log.e("ERROR", "onError: Capture error  ", exception)
                }
            }
        )
    }

}