package com.binarystack01.pix.presentation.ui.screens.mylist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.binarystack01.pix.presentation.ui.components.card.Card
import com.binarystack01.pix.presentation.viewmodel.visionviewmodel.VisionViewModel
import com.binarystack01.pix.ui.theme.GrayNeutral

@Composable
fun MyList(
    visionViewModel: VisionViewModel
) {

    val textRecords by visionViewModel.visionState.collectAsState()

    Surface(
        color = Color.White,
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(space = 8.dp, alignment = Alignment.Top)
        ) {
            item { Spacer(modifier = Modifier.height(4.dp)) }
            items(textRecords.data) { data ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp)
                ) {
                    Column(modifier = Modifier.padding(2.dp)) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 2.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = data.title)
                            Text(
                                text = data.createdAt,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 10.sp,
                                color = GrayNeutral
                            )

                        }
                        Text(buildAnnotatedString {
                            withStyle(style = SpanStyle()) {
                                append(data.text.replace("\n", " "))
                                append("...")
                            }
                        })
                    }
                }
            }
            item { Spacer(modifier = Modifier.height(4.dp)) }
        }
    }
}