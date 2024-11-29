package com.binarystack01.pix.presentation.ui.components.savealert

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun Alert(
    visible: Boolean = false,
    onDismissRequest: () -> Unit = {},
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit = {},
) {
    if (visible) {
        Dialog(onDismissRequest = { onDismissRequest() }) {
            Card(
                modifier = modifier,
                shape = RoundedCornerShape(10.dp)
            ) {
                content()
            }
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AlertPreview() {
    var isVisible by remember { mutableStateOf(false) }
    var changeColor by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier.fillMaxSize(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = if (changeColor) Color.Blue else Color.White),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Alert(
                visible = isVisible,
                modifier = Modifier
                    .height(100.dp)
                    .fillMaxWidth(),
            ) {
                Text("Header")
                Text(
                    text = "I am Blue",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )

                Row {
                    Button(onClick = {
                        isVisible = false
                    }) {
                        Text("Close")
                    }
                    Button(onClick = {
                        changeColor = true
                        isVisible = false
                    }) {
                        Text("Change Color")
                    }
                }
            }

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp),
                onClick = { isVisible = true }
            ) {
                Text("Open")
            }

        }
    }
}