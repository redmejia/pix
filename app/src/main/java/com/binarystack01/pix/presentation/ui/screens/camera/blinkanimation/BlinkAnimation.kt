package com.binarystack01.pix.presentation.ui.screens.camera.blinkanimation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun BlinkAnimation(
    visible: Boolean,
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(
            initialAlpha = 0.0f,
            animationSpec = tween(durationMillis = 300, easing = EaseIn)
        ),
        exit = fadeOut(
            targetAlpha = 0.0f,
            animationSpec = tween(durationMillis = 500, easing = EaseInOut)
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color(0x61000000))
        )
    }
}