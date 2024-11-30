package com.binarystack01.pix

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
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


class MainActivity : ComponentActivity() {

    private lateinit var captureViewModel: CaptureViewModel
    private lateinit var visionViewModel: VisionViewModel
    private lateinit var permissionsViewModel: PermissionsViewModel

    private lateinit var photoRepository: PhotoRepository
    private lateinit var visionRepository: VisionRepository

    private lateinit var photoDao: PhotoDao
    private lateinit var visionDao: VisionDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
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

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        BottomBar(navController = navHostController)
                    }
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
