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
import com.binarystack01.pix.domain.usecases.time.Time
import com.binarystack01.pix.presentation.viewmodel.visionviewmodel.VisionViewModel
import com.binarystack01.pix.presentation.viewmodel.visionviewmodel.VisionViewModel.Companion
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


    val time = Time()

    private val WIDTH = 400
    private val HEIGHT = 400

    // Original size photo directory
    private val PHOTO_DIRECTORY_NAME = "photos"

    // Thumbnail photo directory
    private val THUMBNAIL_DIRECTORY_NAME = "thumbnails"
    private val IMAGE_FORMAT = "jpg"

    private val TIME_FORMAT = "MMM dd, yyyy HH:mm a"

    private fun generateUUID(): String = UUID.randomUUID().toString()
    private fun timeNow(): String = time.timeFormater(format = TIME_FORMAT)


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

        val photsDir = File(context.filesDir, PHOTO_DIRECTORY_NAME)

        if (!photsDir.exists()) {
            photsDir.mkdir()
        }

        val photoFile = File(photsDir, "$fileName.$IMAGE_FORMAT")

        writeAndSave(photoFile, bitmap)

        return photoFile.absolutePath
    }

    private suspend fun saveThumbnail(context: Context, bitmap: Bitmap, fileName: String): String {

        val thumbnailDir = File(context.filesDir, THUMBNAIL_DIRECTORY_NAME)

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

            // Delete items from photo and thumbnail directories
            val isThumbnailDeleted = readAndDeleteFile(thumbnailPath)
            val isPhotoDeleted = readAndDeleteFile(photPath)

            if (isThumbnailDeleted && isPhotoDeleted) {
                photoRepository.deleteImage(photo)
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
                                createdAt = timeNow()
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