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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.binarystack01.pix.R
import com.binarystack01.pix.presentation.ui.components.actionbuttons.CaptureButton
import com.binarystack01.pix.presentation.ui.components.actionbuttons.ControlButton
import com.binarystack01.pix.presentation.ui.components.permissionactions.educational.Educational
import com.binarystack01.pix.presentation.ui.screens.camera.blinkanimation.BlinkAnimation
import com.binarystack01.pix.presentation.ui.screens.camera.controllers.ButtonControllers
import com.binarystack01.pix.presentation.ui.screens.camera.recognitionbox.RecognitionBox
import com.binarystack01.pix.presentation.ui.screens.gallery.Gallery
import com.binarystack01.pix.presentation.viewmodel.captureviewmodel.CaptureViewModel
import com.binarystack01.pix.presentation.viewmodel.permissionsviewmodel.PermissionsViewModel
import kotlinx.coroutines.delay


@Composable
fun Camera(
    permissionsViewModel: PermissionsViewModel = viewModel(),
    captureViewModel: CaptureViewModel = viewModel(),
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

    val detectedText = remember { mutableStateOf("") }
    val selectTextRecognition = remember { mutableStateOf(false) }
    val clicked = remember { mutableStateOf(false) }
    val isBackCameraSelected = remember { mutableStateOf(true) }
    val visibleBlink = remember { mutableStateOf(false) }


    LaunchedEffect(permissionState) {
        if (!permissionState) {
            cameraPermissionResultLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    DisposableEffect(permissionState) {
        if (permissionState) {
            cameraController.setEnabledUseCases(CameraController.IMAGE_CAPTURE)
            cameraController.bindToLifecycle(lifecycleOwner)
        }
        onDispose {
            Log.d("CAMERA", "Unbinding camera")
            cameraController.unbind()
        }
    }

    LaunchedEffect(visibleBlink.value) {
        if (visibleBlink.value) {
            delay(200)
            visibleBlink.value = false
        }
    }

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
            BlinkAnimation(visible = visibleBlink.value)
            Gallery() // Temp this will remove when navigation compose is incorporate
            RecognitionBox(
                context = context,
                cameraController = cameraController,
                selectTextRecognition = selectTextRecognition,
                detectedText = detectedText,
                isBackCameraSelected = isBackCameraSelected
            )
            Box(
                modifier = Modifier.matchParentSize(),
                contentAlignment = Alignment.BottomCenter
            ) {
                ButtonControllers(modifier = Modifier.padding(bottom = 15.dp)) {
                    ControlButton(
                        onClick = {
                            selectTextRecognition.value = !selectTextRecognition.value
                            detectedText.value =
                                if (selectTextRecognition.value) "Detecting...." else ""
                        },
                        painter = R.drawable.round_short_text
                    )
                    CaptureButton(
                        onClick = {
                            clicked.value = !clicked.value

                            visibleBlink.value = true
                            captureViewModel.capturePicture(
                                context = context,
                                cameraController = cameraController,
                            )
                        },
                        clicked = clicked
                    )
                    ControlButton(
                        onClick = {
                            if (cameraController.cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) {
                                isBackCameraSelected.value = false
                                cameraController.cameraSelector =
                                    CameraSelector.DEFAULT_FRONT_CAMERA
                            } else {
                                isBackCameraSelected.value = true
                                cameraController.cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
                            }
                        },
                        painter = R.drawable.rotate_camera
                    )
                }
            }
        }
    }
}