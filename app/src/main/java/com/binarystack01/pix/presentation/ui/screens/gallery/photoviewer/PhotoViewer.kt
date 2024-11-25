package com.binarystack01.pix.presentation.ui.screens.gallery.photoviewer

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import coil3.Image
import com.binarystack01.pix.presentation.ui.components.actionbuttons.CloseButton
import com.binarystack01.pix.ui.theme.BlackPrimary40


@Composable
fun PhotoViewer(
    open: MutableState<Boolean>,
    photo: Bitmap,
) {
    val scale = remember { mutableFloatStateOf(1f) }
    val offsetX = remember { mutableFloatStateOf(0f) }
    val offsetY = remember { mutableFloatStateOf(0f) }

    if (open.value) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = BlackPrimary40)
        ) {
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
                bitmap = photo.asImageBitmap(),
                contentDescription = null
            )

            Box(
                contentAlignment = Alignment.TopStart
            ) {
                CloseButton(onClick = { open.value = !open.value })
            }
        }
    }
}