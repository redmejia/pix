package com.binarystack01.pix.presentation.ui.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.binarystack01.pix.presentation.ui.screens.camera.Camera
import com.binarystack01.pix.presentation.ui.screens.gallery.Gallery
import com.binarystack01.pix.presentation.viewmodel.captureviewmodel.CaptureViewModel
import com.binarystack01.pix.presentation.viewmodel.permissionsviewmodel.PermissionsViewModel
import com.binarystack01.pix.presentation.viewmodel.visionviewmodel.VisionViewModel

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier,
    navHostController: NavHostController,
    captureViewModel: CaptureViewModel,
    visionViewModel: VisionViewModel,
    permissionsViewModel: PermissionsViewModel,
) {

    NavHost(
        modifier = modifier,
        navController = navHostController,
        startDestination = AppScreens.Camera.name
    ) {
        composable(route = AppScreens.Camera.name) {
            Camera(
                permissionsViewModel = permissionsViewModel,
                captureViewModel = captureViewModel,
                visionViewModel = visionViewModel
            )
        }

        composable(route = AppScreens.Gallery.name) {
            Gallery(captureViewModel = captureViewModel)
        }

        composable(route = AppScreens.RecognitionList.name) {
            Column {
                Text(text = "This my list")
            }
        }
    }

}