package com.binarystack01.pix.presentation.ui.screens.mylist.swipe

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.binarystack01.pix.R
import com.binarystack01.pix.presentation.ui.components.card.Card
import com.binarystack01.pix.presentation.ui.screens.mylist.articlecard.ArticleCard
import com.binarystack01.pix.presentation.ui.screens.mylist.articlecard.Header
import com.binarystack01.pix.ui.theme.BlueSecondary60
import com.binarystack01.pix.ui.theme.WhitePrimary0

@Composable
fun SwipeToDelete(
    deleteWidthIconSize: Float = 0.20f,
    deleteBackgroundColor: Color = Color.Red,
    deleteIcon: @Composable () -> Unit = {},
    text: @Composable () -> Unit = {},
    content: @Composable () -> Unit,
) {

    Box(
        contentAlignment = Alignment.Center
    ) {

        Card(
            color = deleteBackgroundColor,
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth(deleteWidthIconSize)
                        .padding(
                            vertical = 4.dp,
                            horizontal = 4.dp
                        ), // Same padding Row wrapper ArticleCard composable
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    deleteIcon()
                    text()
                }
                content()
            }
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun SwipeToDeletePreview() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 8.dp),
        verticalArrangement = Arrangement.Center,
    ) {
        SwipeToDelete(
            deleteIcon = {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "",
                    tint = WhitePrimary0
                )
            },
            text = {
                Text(
                    "Delete",
                    color = WhitePrimary0,
                )
            }
        ) {
            ArticleCard(
                modifier = Modifier
                    .fillMaxWidth(),
                onClick = {

                },
                header = { Header("Test Me") },
                leadingIcon = {
                    Icon(
                        painter = painterResource(R.drawable.outline_article),
                        contentDescription = "article",
                        tint = BlueSecondary60
                    )
                }
            ) {
                Column {
                    Text(buildAnnotatedString {
                        withStyle(style = SpanStyle()) {
                            append("This is a test swipe")
                            append("...")
                        }
                    })
                }
            }
        }
    }
}