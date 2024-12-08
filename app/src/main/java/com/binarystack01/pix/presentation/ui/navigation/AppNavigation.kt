package com.binarystack01.pix.presentation.ui.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.binarystack01.pix.presentation.ui.screens.camera.Camera
import com.binarystack01.pix.presentation.ui.screens.gallery.Gallery
import com.binarystack01.pix.presentation.ui.screens.mylist.MyList
import com.binarystack01.pix.presentation.ui.screens.reader.Reader
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
        startDestination = AppScreens.Camera.name,
        enterTransition = {
            slideInHorizontally(initialOffsetX = { -1000 }) + fadeIn(animationSpec = tween(500))
        }, exitTransition = {
            slideOutHorizontally(targetOffsetX = { -1000 }) + fadeOut(animationSpec = tween(500))
        }
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
            MyList(
                navController = navHostController,
                visionViewModel = visionViewModel
            )
        }

        composable(
            route = AppScreens.Reader.name + "/{id}",
            arguments = listOf(navArgument(name = "id") { type = NavType.LongType })
        ) { backStackEntry ->
            Reader(
                visionViewModel = visionViewModel,
                navController = navHostController,
                id = backStackEntry.arguments?.getLong("id")
            )

        }
    }
}