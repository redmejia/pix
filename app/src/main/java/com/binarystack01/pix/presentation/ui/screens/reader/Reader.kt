package com.binarystack01.pix.presentation.ui.screens.reader

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.NavHostController
import com.binarystack01.pix.presentation.ui.components.actionbuttons.CloseButton
import com.binarystack01.pix.presentation.ui.navigation.AppScreens
import com.binarystack01.pix.presentation.viewmodel.visionviewmodel.VisionViewModel
import com.binarystack01.pix.ui.theme.WhitePrimary0

@Composable
fun Reader(
    visionViewModel: VisionViewModel,
    navController: NavHostController,
    id: Long? = null,
) {
    val text by visionViewModel.visionState.collectAsState()
    val view = LocalView.current

    val window = (view.context as Activity).window
    val windowInsetsController = WindowCompat.getInsetsController(window, view)

    LaunchedEffect(key1 = id, key2 = visionViewModel.isReadMode()) {
        if (id != null) {
            visionViewModel.getText(id)
        }

        if (!view.isInEditMode) {
            if (visionViewModel.isReadMode()) {
                windowInsetsController.systemBarsBehavior =
                    WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
                windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())
            }
        }
    }

    // Back system bar navigation
    BackHandler(enabled = true) {
        visionViewModel.resetReaderMode()

        if(!view.isInEditMode){
            windowInsetsController.show(WindowInsetsCompat.Type.systemBars())
        }

        navController.navigate(route = AppScreens.RecognitionList.name) {
            popUpTo(AppScreens.Reader.name) {
                inclusive = true
                saveState = false
            }
            launchSingleTop = true
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = WhitePrimary0)
                .verticalScroll(rememberScrollState())
        ) {
            Box(
               modifier =   Modifier.fillMaxWidth(),
                contentAlignment = Alignment.TopStart
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CloseButton(
                        onClick = {
                            visionViewModel.resetReaderMode()
                            windowInsetsController.show(WindowInsetsCompat.Type.systemBars())
                            navController.navigate(route = AppScreens.RecognitionList.name) {
                                popUpTo(AppScreens.Reader.name) {
                                    inclusive = true
                                    saveState = false
                                }
                                launchSingleTop = true
                            }
                        },
                    )
                }
            }
            text.text?.let { vision ->
                Text(
                    text = vision.title,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 20.sp
                )
            }
            text.text?.let { vision -> Text(text = vision.text) }
        }
    }
}