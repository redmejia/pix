package com.binarystack01.pix.presentation.ui.screens.camera

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.binarystack01.pix.presentation.ui.components.permissionactions.educational.Educational
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


    DisposableEffect(permissionState) {
        if (permissionState) {
            Log.d("CAMERA", "Setting up camera use cases")
            cameraController.setEnabledUseCases(CameraController.IMAGE_CAPTURE)
            cameraController.bindToLifecycle(lifecycleOwner)
        }
        onDispose {
            Log.d("CAMERA", "Unbinding camera")
            cameraController.unbind()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
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
            // Not implemented yet
//            Box(
//                modifier = Modifier
//                    .padding(bottom = 20.dp)
//                    .matchParentSize(),
//                contentAlignment = Alignment.BottomCenter
//            ) {
//                CaptureButton(
//                    onClick = {
//                        clicked.value = !clicked.value
//                        visibleBlink.value = true
//
//                        captureViewModel.capturePicture(
//                            context = context,
//                            controller = cameraController
//                        )
//                    },
//                    clicked = clicked
//                )
//            }
        }
    }

}