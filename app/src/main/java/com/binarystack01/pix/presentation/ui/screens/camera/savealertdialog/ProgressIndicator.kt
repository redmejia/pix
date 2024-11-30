package com.binarystack01.pix.presentation.ui.screens.camera.savealertdialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.binarystack01.pix.ui.theme.BluePrimary40
import com.binarystack01.pix.ui.theme.WhitePrimary95
import kotlinx.coroutines.delay


suspend fun loadProgressIndicator(updateProgress: (Float) -> Unit) {
    for (i in 1..100) {
        updateProgress(i.toFloat() / 100)
        delay(100)
    }
}

@Composable
fun ProgressIndicator(
    isLoading: Boolean,
    currentProgress: Float,
) {
    if (isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = WhitePrimary95),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                color = BluePrimary40,
                progress = { currentProgress },
            )
        }
    }
}