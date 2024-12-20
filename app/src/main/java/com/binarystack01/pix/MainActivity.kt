package com.binarystack01.pix

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.binarystack01.pix.data.local.AppDatabase
import com.binarystack01.pix.data.local.room.dao.PhotoDao
import com.binarystack01.pix.data.local.room.dao.VisionDao
import com.binarystack01.pix.data.repositories.room.PhotoRepository
import com.binarystack01.pix.data.repositories.room.VisionRepository
import com.binarystack01.pix.presentation.ui.navigation.AppNavigation
import com.binarystack01.pix.presentation.ui.navigation.BottomBar
import com.binarystack01.pix.presentation.viewmodel.captureviewmodel.CaptureViewModel
import com.binarystack01.pix.presentation.viewmodel.permissionsviewmodel.PermissionsViewModel
import com.binarystack01.pix.presentation.viewmodel.visionviewmodel.VisionViewModel
import com.binarystack01.pix.ui.theme.PixTheme
import kotlinx.coroutines.flow.map
import android.graphics.Color
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.binarystack01.pix.presentation.ui.components.animation.swipe.DraggableBox
import com.binarystack01.pix.presentation.ui.screens.camera.Camera
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {

    private lateinit var captureViewModel: CaptureViewModel
    private lateinit var visionViewModel: VisionViewModel
    private lateinit var permissionsViewModel: PermissionsViewModel

    private lateinit var photoRepository: PhotoRepository
    private lateinit var visionRepository: VisionRepository

    private lateinit var photoDao: PhotoDao
    private lateinit var visionDao: VisionDao

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)

        val splashScreen = installSplashScreen()
        var keepSplashScreen = true

        super.onCreate(savedInstanceState)
        splashScreen.setKeepOnScreenCondition { keepSplashScreen }


        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT),
        )
        setContent {

            PixTheme {

                val db = AppDatabase.getInstance(context = applicationContext)
                photoDao = db.photoDao()
                visionDao = db.visionDao()

                photoRepository = PhotoRepository(photoDao)
                visionRepository = VisionRepository(visionDao)

                captureViewModel = viewModel { CaptureViewModel(photoRepository) }
                visionViewModel = viewModel { VisionViewModel(visionRepository) }

                permissionsViewModel = viewModel<PermissionsViewModel>()

                val navHostController = rememberNavController()

                LaunchedEffect(Unit) {
                    delay(200)
                    keepSplashScreen = false
                }

                val isScaffoldVisible = remember { mutableStateOf(true) }

                Box {

                    val configuration = LocalConfiguration.current
                    val screenWidth = configuration.screenWidthDp.toFloat()

                    // main scaffold animation
                    val translationX = remember { Animatable(0f) }
                    translationX.updateBounds(0f)

                    val coroutine = rememberCoroutineScope()

                    val draggableState = rememberDraggableState(onDelta = { dragAmount ->
                        coroutine.launch {
                            translationX.snapTo(translationX.value + dragAmount)
                        }
                    })


                    val cameraTranslationX = remember { Animatable(0f) }
                    cameraTranslationX.updateBounds(0f)


                    val decay = rememberSplineBasedDecay<Float>()


                    DraggableBox(
                        modifier = Modifier
                            .graphicsLayer {
                                this.translationX = cameraTranslationX.value
                            },
                        coroutine = coroutine,
                        translationX = translationX,
                        draggableState = draggableState,
                        screenWidth = screenWidth,
                        decay = decay,
                        visibleContent = isScaffoldVisible,
                        onDragThreshold = 0.5f

                    ) {
                        Camera(
                            permissionsViewModel = permissionsViewModel,
                            captureViewModel = captureViewModel,
                            visionViewModel = visionViewModel
                        )
                    }


                    DraggableBox(
                        modifier = Modifier
                            .graphicsLayer {
                                this.translationX = translationX.value
                            },
                        coroutine = coroutine,
                        translationX = translationX,
                        draggableState = draggableState,
                        screenWidth = screenWidth,
                        decay = decay,
                        visibleContent = isScaffoldVisible,
                        onDragThreshold = 0.5f

                    ) {
                        if (isScaffoldVisible.value) {
                            Scaffold(
                                modifier = Modifier.fillMaxSize(),
                                bottomBar = {
                                    val readMode by visionViewModel.visionState
                                        .map { it.readerMode }
                                        .collectAsState(initial = false)

                                    if (!readMode) {
                                        BottomBar(navController = navHostController)
                                    }
                                },
                                contentWindowInsets = WindowInsets(0.dp)
                            ) { innerPadding ->
                                AppNavigation(
                                    modifier = Modifier.padding(innerPadding),
                                    navHostController = navHostController,
                                    captureViewModel = captureViewModel,
                                    visionViewModel = visionViewModel,
                                    permissionsViewModel = permissionsViewModel
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

