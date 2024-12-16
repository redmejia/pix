package com.binarystack01.pix.presentation.ui.components.actionbuttons

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

// Any case of use for action add, update, change and delete
@Composable
fun ActionButton(
    onClick: () -> Unit = {},
    colors: IconButtonColors = IconButtonDefaults.iconButtonColors(),
    imageIconVector: ImageVector = Icons.Default.Done,
) {
    IconButton(
        onClick = { onClick() },
        colors = colors,
    ) {
        Icon(imageVector = imageIconVector, contentDescription = "action")
    }
}