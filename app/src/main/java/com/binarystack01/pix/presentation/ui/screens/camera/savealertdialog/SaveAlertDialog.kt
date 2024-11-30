package com.binarystack01.pix.presentation.ui.screens.camera.savealertdialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.binarystack01.pix.presentation.ui.components.alert.Alert
import com.binarystack01.pix.presentation.ui.components.textinput.TextInput
import com.binarystack01.pix.presentation.viewmodel.visionviewmodel.VisionViewModel
import com.binarystack01.pix.ui.theme.BlackPrimary0
import com.binarystack01.pix.ui.theme.BluePrimary40
import com.binarystack01.pix.ui.theme.BluePrimary50
import kotlinx.coroutines.launch

@Composable
private fun ButtonRowGroup(
    content: @Composable () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.End,
    ) {
        content()
    }
}

@Composable
fun SaveAlertDialog(
    visionViewModel: VisionViewModel,
    detectedText: String,
    visible: Boolean,
    onDismiss: () -> Unit,
) {

    var input by rememberSaveable { mutableStateOf("") }

    var currentProgress by remember { mutableFloatStateOf(0f) }
    var loading by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()

    Alert(
        visible = visible,
        onDismissRequest = { if (!loading) onDismiss() },
        modifier = Modifier
            .height(200.dp)
            .fillMaxWidth(),
        content = {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.SpaceEvenly
                ) {
                    Text(
                        "Save Text",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                    TextInput(
                        icon = {
                            Icon(
                                Icons.Outlined.Edit,
                                contentDescription = "edit",
                                tint = BlackPrimary0
                            )
                        },
                        placeHolder = { Text("Title") },
                        color = BluePrimary50,
                        value = input,
                        onChange = { newVal -> input = newVal },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 2.dp)
                            .shadow(
                                elevation = 6.dp,
                                spotColor = BluePrimary40,
                                shape = RoundedCornerShape(10.dp)
                            )
                    )
                    ButtonRowGroup {
                        TextButton(
                            enabled = !loading,
                            onClick = { onDismiss() }) { Text("Close") }
                        TextButton(
                            enabled = !loading,
                            onClick = {
                                loading = true
                                scope.launch {
                                    visionViewModel.saveVisionText(
                                        title = input,
                                        text = detectedText
                                    )
                                    loadProgressIndicator { progress -> currentProgress = progress }
                                    input = ""
                                    loading = false
                                    onDismiss()
                                }
                            }) { Text("Save") }
                    }
                }
                ProgressIndicator(isLoading = loading, currentProgress = currentProgress)
            }
        }
    )
}