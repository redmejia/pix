package com.binarystack01.pix.presentation.ui.screens.camera

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineBreak
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.binarystack01.pix.R
import com.binarystack01.pix.domain.usecases.analyzer.TextImageAnalyzer
import com.binarystack01.pix.presentation.ui.components.actionbuttons.CaptureButton
import com.binarystack01.pix.presentation.ui.components.actionbuttons.ControlButton
import com.binarystack01.pix.presentation.ui.components.permissionactions.educational.Educational
import com.binarystack01.pix.presentation.ui.components.textbox.TextBox
import com.binarystack01.pix.presentation.viewmodel.permissionsviewmodel.PermissionsViewModel


@Composable
fun Camera(
    permissionsViewModel: PermissionsViewModel = viewModel(),
) {

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraController = remember { LifecycleCameraController(context) }


    val deniedPermission = remember { mutableStateOf(false) }

    val cameraPermissionResultLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                permissionsViewModel.onCameraPermission(
                    permission = Manifest.permission.CAMERA,
                    isGranted = true
                )

            }
            // Permission not granted THEN display warning message USER change permission on Pix
            // App settings
            if (permissionsViewModel.shouldShowRequestPermission(
                    context,
                    Manifest.permission.CAMERA
                )
            ) {
                deniedPermission.value = true
            }
        }
    )

    val permissionState by permissionsViewModel.permissionState.collectAsState()

    LaunchedEffect(permissionState) {
        if (!permissionState) {
            cameraPermissionResultLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    val detectedText = remember { mutableStateOf("No text detected yet..") }
    val selectTextRecognition = remember { mutableStateOf(false) }

    LaunchedEffect(selectTextRecognition.value) {

//        if (selectTextRecognition.value) {
//            cameraController.setImageAnalysisAnalyzer(
//                ContextCompat.getMainExecutor(context),
//                TextImageAnalyzer(onDetectedText = {
//                    detectedText.value = it
//                    Log.i("MY-TEXT >>>", "Camera: $it")
//                })
//            )
//        }
    }

    DisposableEffect(permissionState) {
        if (permissionState) {
            cameraController.setEnabledUseCases(CameraController.IMAGE_CAPTURE or CameraController.IMAGE_ANALYSIS)
            cameraController.setImageAnalysisAnalyzer(
                ContextCompat.getMainExecutor(context),
                TextImageAnalyzer(onDetectedText = {
                    detectedText.value = it
                    Log.i("MY-TEXT >>>", "Camera: $it")
                })
            )
            cameraController.bindToLifecycle(lifecycleOwner)
        }
        onDispose {
            Log.d("CAMERA", "Unbinding camera")
            cameraController.unbind()
        }
    }

    Log.d("SAVE", "Camera: ${detectedText.value}")
    val clicked = remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        // USER denied permission WARNING message OPEN SETTING to grant permission
        if (deniedPermission.value) {
            Educational(
                onClick = {
                    val intent = Intent(
                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.fromParts("package", context.packageName, null)
                    )
                    context.startActivity(intent)
                },
                buttonTitle = "Open Settings"
            )
        }
        // Permission GRANTED
        if (permissionState) {
            AndroidView(
                factory = { ctx ->
                    PreviewView(ctx).apply {
                        scaleType = PreviewView.ScaleType.FILL_CENTER
                        implementationMode = PreviewView.ImplementationMode.COMPATIBLE
                        controller = cameraController
                    }
                },
                modifier = Modifier.fillMaxSize()
            )
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
                            fontSize = 10.sp,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
            Box(
                modifier = Modifier
                    .padding(bottom = 20.dp)
                    .matchParentSize(),
                contentAlignment = Alignment.BottomCenter
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    ControlButton(
                        onClick = {
                            selectTextRecognition.value = true
                        },
                        painter = R.drawable.round_short_text
                    )
                    CaptureButton(
                        onClick = {
                            clicked.value = !clicked.value
                            // not implemented yet
//                        visibleBlink.value = true
//
//                        captureViewModel.capturePicture(
//                            context = context,
//                            controller = cameraController
//                        )
                        },
                        clicked = clicked
                    )
                    ControlButton(
                        onClick = {
                            cameraController.cameraSelector =
                                if (cameraController.cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) {
                                    CameraSelector.DEFAULT_FRONT_CAMERA
                                } else {
                                    CameraSelector.DEFAULT_BACK_CAMERA
                                }
                        },
                        painter = R.drawable.rotate_camera
                    )
                }
            }
        }
    }
}