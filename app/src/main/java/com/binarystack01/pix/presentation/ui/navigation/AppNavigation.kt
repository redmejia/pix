package com.binarystack01.pix.presentation.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.binarystack01.pix.presentation.ui.screens.camera.Camera
import com.binarystack01.pix.presentation.ui.screens.gallery.Gallery
import com.binarystack01.pix.presentation.viewmodel.captureviewmodel.CaptureViewModel
import com.binarystack01.pix.presentation.viewmodel.permissionsviewmodel.PermissionsViewModel

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier,
    navHostController: NavHostController,
    captureViewModel: CaptureViewModel,
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
                captureViewModel = captureViewModel
            )
        }

        composable(route = AppScreens.Gallery.name) {
            Gallery(captureViewModel = captureViewModel)
        }
    }

}