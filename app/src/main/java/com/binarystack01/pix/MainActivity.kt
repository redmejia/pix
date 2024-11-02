package com.binarystack01.pix

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.binarystack01.pix.presentation.ui.components.alerts.PermissionAlert
import com.binarystack01.pix.presentation.viewmodel.permissionsviewmodel.PermissionsViewModel
import com.binarystack01.pix.ui.theme.PixTheme

class MainActivity : ComponentActivity() {
    private lateinit var permissionsViewModel: PermissionsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PixTheme {

                val context = LocalContext.current

                permissionsViewModel = viewModel<PermissionsViewModel>()


                val cameraPermissionResultLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.RequestPermission(),
                    onResult = { isGranted ->
                        permissionsViewModel.onCameraPermission(
                            permission = Manifest.permission.CAMERA,
                            isGranted = isGranted
                        )
                    }
                )

                val permissionState by permissionsViewModel.permissionState.collectAsState()

                LaunchedEffect(permissionState) {
                    Log.d("PER", "onCreate: $permissionState")
                    if (!permissionState) {
                        cameraPermissionResultLauncher.launch(Manifest.permission.CAMERA)
                    }
                }

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(modifier = Modifier.padding(innerPadding)) {

                        if (permissionsViewModel.isPermissionGranted(
                                context,
                                Manifest.permission.CAMERA
                            )
                        ) {
                            PermissionAlert(
                                onDismissRequest = { /*TODO*/ },
                                onConfirmation = {
                                    val intent = Intent(
                                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                        Uri.fromParts("package", context.packageName, null)
                                    )
                                    context.startActivity(intent)
                                },
                                dialogTitle = "Permission request",
                                dialogText = "S",
                                icon = Icons.Default.Warning
                            )
                        }
                    }
                }
            }
        }
    }
}
