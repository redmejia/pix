package com.binarystack01.pix.presentation.ui.components.permissionactions.educational

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.binarystack01.pix.ui.theme.BlackPrimary40
import com.binarystack01.pix.ui.theme.BluePrimary40
import com.binarystack01.pix.ui.theme.BluePrimary50
import com.binarystack01.pix.ui.theme.BlueSecondary60


@Composable
fun Educational(
    onClick: () -> Unit = {},
    buttonTextColor: Color = BluePrimary40,
    buttonTitle: String = "Click",
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = BlackPrimary40),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Icon(
                modifier = Modifier.size(40.dp),
                imageVector = Icons.Default.Warning,
                contentDescription = null,
                tint = BluePrimary50
            )
            Text(
                buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            color = BlueSecondary60,
                            fontWeight = FontWeight.Normal,
                            fontSize = 25.sp
                        )
                    ) {
                        append("Permission required\n")
                    }
                    withStyle(
                        style = SpanStyle(
                            color = BlueSecondary60,
                            fontSize = 15.sp
                        )
                    ) {
                        append("To use this feature, please grant camera permission in your device settings.\n")
                    }
                }
            )
            TextButton(
                modifier = Modifier.align(Alignment.End),
                onClick = { onClick() }
            ) {
                Text(
                    text = buttonTitle,
                    color = buttonTextColor,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun EducationalPreview() {
    Educational()
}