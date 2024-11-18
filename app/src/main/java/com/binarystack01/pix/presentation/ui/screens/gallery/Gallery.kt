package com.binarystack01.pix.presentation.ui.screens.gallery

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.binarystack01.pix.presentation.viewmodel.captureviewmodel.CaptureViewModel
import com.binarystack01.pix.ui.theme.BlackPrimary40


@Composable
fun Gallery(
    captureViewModel: CaptureViewModel,
) {
    val photoState by captureViewModel.photoState.collectAsState()
    val selectPhotoId = remember { mutableStateOf("") }

    val scale = remember { mutableFloatStateOf(1f) }
    val offsetX = remember { mutableFloatStateOf(0f) }
    val offsetY = remember { mutableFloatStateOf(0f) }

    LaunchedEffect(selectPhotoId.value) {
        if (selectPhotoId.value.isNotEmpty()) {
            captureViewModel.loadPhoto(selectPhotoId.value)
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (photoState.photos.isEmpty()) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = "No Photo.")
            }
        } else {
            Box(modifier = Modifier.fillMaxSize()) {
                LazyVerticalStaggeredGrid(
                    columns = StaggeredGridCells.Fixed(3),
                    verticalItemSpacing = 4.dp,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    content = {
                        items(photoState.photos) { photo ->
                            Image(
                                modifier = Modifier.clickable {
                                    selectPhotoId.value = photo.id
                                },
                                bitmap = photo.photo.asImageBitmap(), contentDescription = null
                            )
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                )
                // test box
                if (photoState.photo?.id?.isNotEmpty() == true) {
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .background(color = BlackPrimary40)
                    ) {
                        photoState.photo?.let { photo ->
                            Image(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .graphicsLayer {
                                        scaleX = scale.floatValue
                                        scaleY = scale.floatValue
                                        translationX = offsetX.floatValue
                                        translationY = offsetY.floatValue
                                    }
                                    .pointerInput(Unit) {
                                        detectTransformGestures { _, _, zoom, _ ->
                                            scale.floatValue =
                                                (scale.floatValue * zoom).coerceIn(1f, 3f)
                                        }
                                    },
                                bitmap = photo.photo.asImageBitmap(),
                                contentDescription = null
                            )
                        }
                    }
                }
            }
        }
    }
}