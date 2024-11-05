package com.binarystack01.pix.presentation.ui.components.actionbuttons

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun CaptureButton(
    onClick: () -> Unit = {},
    clicked: MutableState<Boolean>,
) {

    val scale by animateFloatAsState(
        targetValue = if (!clicked.value) 1.2f else 1f,
        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing),
        label = "alpha"
    )

    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = Modifier
            .scale(scale)
            .clickable(
                indication = null,
                interactionSource = interactionSource,
            ) {
                onClick()
            },
        contentAlignment = Alignment.Center
    ) {
        // outer circle
        Box(
            modifier = Modifier
                .size(60.dp)
                .border(width = 3.dp, color = Color(0xFFD9D9D9), shape = CircleShape)
                .background(color = Color.Black, CircleShape)
        )
        // inner circle
        Box(
            modifier = Modifier
                .size(50.dp)
                .background(color = Color(0xFFD9D9D9), shape = CircleShape)
        )
    }

    LaunchedEffect(clicked.value) {
        if (clicked.value) {
            delay(300)
            clicked.value = false
        }
    }
}
