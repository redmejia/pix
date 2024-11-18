package com.binarystack01.pix.presentation.ui.components.actionbuttons

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.binarystack01.pix.ui.theme.BlackPrimary95
import com.binarystack01.pix.ui.theme.WhitePrimary0
import com.binarystack01.pix.R

@Composable
fun ControlButton(
    onClick: () -> Unit = {},
    colors: IconButtonColors = IconButtonDefaults
        .iconButtonColors(
            containerColor = BlackPrimary95,
            contentColor = WhitePrimary0,
            disabledContentColor = WhitePrimary0,
            disabledContainerColor = BlackPrimary95
        ),
    painter: Int = R.drawable.rotate_camera,
) {
    IconButton(
        onClick = { onClick() },
        colors = colors,
    ) {
        Icon(painter = painterResource(id = painter), contentDescription = "action")
    }
}

@Preview(showSystemUi = true)
@Composable
fun ControlButtonPreview() {
    ControlButton()
}