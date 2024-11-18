package com.binarystack01.pix.presentation.ui.components.actionbuttons

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import com.binarystack01.pix.ui.theme.BlackPrimary95
import com.binarystack01.pix.ui.theme.WhitePrimary0

@Composable
fun CloseButton(
    onClick: () -> Unit = {},
    colors: IconButtonColors = IconButtonDefaults
        .iconButtonColors(
            containerColor = BlackPrimary95,
            contentColor = WhitePrimary0,
            disabledContentColor = WhitePrimary0,
            disabledContainerColor = BlackPrimary95
        ),
) {
    IconButton(
        onClick = { onClick() },
        colors = colors,
    ) {
        Icon(imageVector = Icons.Default.Close, contentDescription = "close action")
    }
}