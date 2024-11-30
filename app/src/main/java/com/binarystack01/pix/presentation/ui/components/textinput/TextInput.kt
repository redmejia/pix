package com.binarystack01.pix.presentation.ui.components.textinput

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.binarystack01.pix.ui.theme.BlackPrimary0
import com.binarystack01.pix.ui.theme.BluePrimary40
import com.binarystack01.pix.ui.theme.BluePrimary50

@Composable
fun TextInput(
    icon: @Composable () -> Unit = {},
    placeHolder: @Composable () -> Unit = {},
    value: String = "",
    onChange: (String) -> Unit = {},
    color: Color = BluePrimary50,
    modifier: Modifier = Modifier,
) {
    OutlinedTextField(
        modifier = modifier,
        placeholder = { placeHolder() },
        leadingIcon = { icon() },
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.Sentences
        ),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = color,
            unfocusedContainerColor = color,

            focusedBorderColor = color,
            unfocusedBorderColor = color,

            cursorColor = BlackPrimary0
        ),
        value = value,
        onValueChange = { newValue ->
            onChange(newValue)
        },
        shape = RoundedCornerShape(10.dp)
    )
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun TextInputPreview() {
    var input by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        TextInput(
            icon = {
                Icon(
                    Icons.Outlined.Edit,
                    contentDescription = "edit",
                    tint = BlackPrimary0
                )
            },
            color = BluePrimary50,
            value = input,
            onChange = { newVal -> input = newVal },
            modifier = Modifier
                .fillMaxWidth()
                .shadow(
                    elevation = 6.dp,
                    spotColor = BluePrimary40,
                    shape = RoundedCornerShape(10.dp)
                )
        )
    }
}