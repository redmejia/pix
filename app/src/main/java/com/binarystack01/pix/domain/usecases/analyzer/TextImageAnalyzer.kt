package com.binarystack01.pix.domain.usecases.analyzer

import android.util.Log
import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions


class TextImageAnalyzer(
    val onDetectedText: (String) -> Unit,
) : ImageAnalysis.Analyzer {

    private val textRecognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

    @OptIn(ExperimentalGetImage::class)
    override fun analyze(imageProxy: ImageProxy) {

        val image = imageProxy.image

        if (image != null) {
            val inputImage =
                InputImage.fromMediaImage(image, imageProxy.imageInfo.rotationDegrees)

            textRecognizer.process(inputImage)
                .addOnSuccessListener { visionText ->

                    val text: String = visionText.text

                    if (text.isNotBlank()) {
                        onDetectedText(text)
                    }
                }
                .addOnFailureListener { e ->
                    Log.e("ERROR", "analyze: Image analyzing error: $e")
                }
                .addOnCompleteListener {
                    Log.d("CLOSE", "analyze: close")
                    imageProxy.close()
                }
        } else {
            imageProxy.close()
        }
    }
}