package com.binarystack01.pix.presentation.ui.screens.gallery

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.lifecycle.viewmodel.compose.viewModel
import com.binarystack01.pix.presentation.viewmodel.captureviewmodel.CaptureViewModel
import kotlinx.coroutines.delay


@Composable
fun Gallery(
    captureViewModel: CaptureViewModel = viewModel(),
) {
    val imageList by captureViewModel.imageListState.collectAsState()
    val hasImages = remember { mutableStateOf(false) }

    LaunchedEffect(key1 = imageList) {
        if (imageList.isNotEmpty()) {
            delay(3000L)
            hasImages.value = true
        }

    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (hasImages.value) {
            Image(bitmap = imageList[0].asImageBitmap(), contentDescription = "images")
        }
    }
}
