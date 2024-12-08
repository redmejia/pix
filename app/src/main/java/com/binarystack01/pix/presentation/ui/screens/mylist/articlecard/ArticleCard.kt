package com.binarystack01.pix.presentation.ui.screens.mylist.articlecard

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.binarystack01.pix.R
import com.binarystack01.pix.presentation.ui.components.card.Card
import com.binarystack01.pix.ui.theme.BlueSecondary60
import com.binarystack01.pix.ui.theme.GrayNeutral
import com.binarystack01.pix.ui.theme.WhitePrimary0

// Header for Article Card or create custom Composable for your design
@Composable
fun Header(
    title: String = "My title",
    createdAt: String = "08-20-24",
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = title,
            fontWeight = FontWeight.Bold,
        )
        Text(
            text = createdAt,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 11.sp,
            color = BlueSecondary60,
            textAlign = TextAlign.End
        )

    }
}

@Composable
fun ArticleCard(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    cardColor: Color = WhitePrimary0,
    elevation: Dp = 3.dp,
    leadingIcon: @Composable () -> Unit = {},
    header: @Composable () -> Unit = {},
    body: @Composable () -> Unit = {},

    ) {
    Card(
        modifier = modifier,
        color = cardColor,
        elevation = CardDefaults.cardElevation(
            defaultElevation = elevation
        )
    ) {
        // wrapper
        Row(
            modifier = Modifier
                .clickable { onClick() }
                .padding(vertical = 4.dp, horizontal = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Leading Icon
            Column(
                modifier = Modifier.padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                leadingIcon()
            }
            // content
            Column {
                header()
                body()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ArticleCardPreview() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        ArticleCard(
            cardColor = GrayNeutral,
            modifier = Modifier.fillMaxWidth(),
            header = { Header("My Text") },
            leadingIcon = {
                Icon(
                    painter = painterResource(R.drawable.outline_article),
                    contentDescription = "article",
                    tint = BlueSecondary60
                )
            }
        ) {
            Text("This")
        }
    }
}
