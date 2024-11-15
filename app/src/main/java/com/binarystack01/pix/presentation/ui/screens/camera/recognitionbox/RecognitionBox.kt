package com.binarystack01.pix.presentation.ui.screens.camera.recognitionbox

import android.content.Context
import android.util.Log
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineBreak
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.binarystack01.pix.domain.usecases.analyzer.TextImageAnalyzer
import com.binarystack01.pix.presentation.ui.components.textbox.TextBox

@Composable
fun RecognitionBox(
    context: Context,
    cameraController: LifecycleCameraController,
    detectedText: MutableState<String>,
    selectTextRecognition: MutableState<Boolean>,
    isBackCameraSelected: MutableState<Boolean>
) {


    LaunchedEffect(selectTextRecognition.value, isBackCameraSelected.value) {
        if (selectTextRecognition.value && isBackCameraSelected.value) {

            cameraController.setEnabledUseCases(CameraController.IMAGE_ANALYSIS)

            cameraController.setImageAnalysisAnalyzer(
                ContextCompat.getMainExecutor(context),
                TextImageAnalyzer(onDetectedText = { text ->
                    detectedText.value = text
                    Log.i("MY-TEXT >>>", "Camera: $text")
                })
            )

        } else {
            cameraController.clearImageAnalysisAnalyzer()
            cameraController.setEnabledUseCases(CameraController.IMAGE_CAPTURE)
            detectedText.value = ""
        }

    }

    if (selectTextRecognition.value && isBackCameraSelected.value) {
        Box(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxSize(),
        ) {
            TextBox(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                ) {
                    Text(
                        text = buildAnnotatedString {
                            withStyle(
                                style = ParagraphStyle(
                                    textAlign = TextAlign.Justify,
                                    lineBreak = LineBreak.Paragraph,
                                )
                            ) {
                                append(detectedText.value)
                            }
                        },
                        fontSize = 14.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }

}