package com.binarystack01.pix.presentation.ui.screens.mylist

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.NavHostController
import com.binarystack01.pix.presentation.ui.navigation.AppScreens
import com.binarystack01.pix.presentation.viewmodel.visionviewmodel.VisionViewModel
import com.binarystack01.pix.R
import com.binarystack01.pix.presentation.ui.screens.mylist.articlecard.ArticleCard
import com.binarystack01.pix.presentation.ui.screens.mylist.articlecard.Header
import com.binarystack01.pix.ui.theme.BlueSecondary60
import com.binarystack01.pix.ui.theme.WhitePrimary0

@Composable
fun MyList(
    navController: NavHostController,
    visionViewModel: VisionViewModel,
) {

    val textRecords by visionViewModel.visionState.collectAsState()
    val view = LocalView.current

    val window = (view.context as Activity).window
    val windowInsetsController = WindowCompat.getInsetsController(window, view)


    LaunchedEffect(visionViewModel.isReadMode()) {
        if (!view.isInEditMode) {
            if (visionViewModel.isReadMode()) {
                windowInsetsController.systemBarsBehavior =
                    WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
                windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())
            }
            //else {
            //    windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())
            //}
        }

    }

    if (textRecords.data.isEmpty()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // TODO: Add an icon or text when text record list is empty
        }
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(color = WhitePrimary0),
            contentPadding = WindowInsets.statusBars.asPaddingValues(), // Respect system bars insets
            verticalArrangement = Arrangement.spacedBy(space = 14.dp, alignment = Alignment.Top)
        ) {
            item { Spacer(modifier = Modifier.height(4.dp)) }

            items(textRecords.data) { data ->
                ArticleCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    onClick = {
                        visionViewModel.changeMode {
                            navController.navigate(route = AppScreens.Reader.name + "/${data.id}")
                        }
                    },
                    header = { Header(data.title, createdAt = data.createdAt) },
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
                                append(data.text.replace("\n", " ".trim()))
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