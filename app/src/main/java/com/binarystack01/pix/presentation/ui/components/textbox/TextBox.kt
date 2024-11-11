package com.binarystack01.pix.presentation.ui.components.textbox

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.binarystack01.pix.ui.theme.BlackPrimary95


// Text Box Composable will be use to display the Recognized text from image analyzer.
@Composable
fun TextBox(
    modifier: Modifier = Modifier,
    contentAlignment: Alignment = Alignment.TopStart,
    content: @Composable () -> Unit,
) {

    Box(
        modifier = modifier
            .background(BlackPrimary95, shape = RoundedCornerShape(5.dp))
            .padding(4.dp),
        contentAlignment = contentAlignment
    ) {
        content()
    }
}

@Preview(showSystemUi = true)
@Composable
fun TexBoxPreview() {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        TextBox(
            modifier = Modifier.matchParentSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            Column {
                Text(text = "Uno")
                Text(text = "Dos")
                Text(text = "Tres")
                Text(text = "Five")
                Text(text = "Hello, dos")
            }
        }
    }

}