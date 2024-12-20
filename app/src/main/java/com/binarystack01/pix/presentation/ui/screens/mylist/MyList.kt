package com.binarystack01.pix.presentation.ui.screens.mylist

import android.app.Activity
import android.util.Log
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.calculateTargetValue
import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
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
import com.binarystack01.pix.presentation.ui.screens.mylist.swipe.SwipeToDelete
import com.binarystack01.pix.ui.theme.BlueSecondary60
import com.binarystack01.pix.ui.theme.WhitePrimary0
import kotlinx.coroutines.launch

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
            Icon(
                modifier = Modifier
                    .size(50.dp),
                painter = painterResource(R.drawable.outline_library_text),
                contentDescription = "",
                tint = BlueSecondary60
            )
        }
    } else {

        val configuration = LocalConfiguration.current
        val screenWidth = configuration.screenWidthDp.toFloat()



        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(color = WhitePrimary0),
            contentPadding = WindowInsets.statusBars.asPaddingValues(), // Respect system bars insets
            verticalArrangement = Arrangement.spacedBy(space = 14.dp, alignment = Alignment.Top)
        ) {
            item { Spacer(modifier = Modifier.height(4.dp)) }

            items(
                textRecords.data,
                key = { it.id!!.toLong() }
            ) { data ->

                val translationX = remember { Animatable(0f) }
                translationX.updateBounds(0f, screenWidth / 3f)

                val coroutine = rememberCoroutineScope()
                val draggableState = rememberDraggableState(onDelta = { dragAmount ->
                    coroutine.launch {
                        translationX.snapTo(translationX.value + dragAmount)
                    }
                })

                val decay = rememberSplineBasedDecay<Float>()

                Box {
                    SwipeToDelete(
                        modifier = Modifier.padding(horizontal = 8.dp),
                        onClick = {
                            Log.d("CLICKED", "MyList: ${data.id}")
                            visionViewModel.deleteVisionTextRecord(vision = data)
                        },
                        deleteBackgroundColor = Color.Red,
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
                                .graphicsLayer {
                                    this.translationX = translationX.value
                                }
                                .draggable(
                                    state = draggableState,
                                    orientation = Orientation.Horizontal,
                                    onDragStopped = { velocity ->

                                        val decayX = decay.calculateTargetValue(
                                            translationX.value,
                                            velocity
                                        )

                                        val targetX = if (decayX > screenWidth / 2f) {
                                            screenWidth
                                        } else {
                                            0f
                                        }

                                        val reachTargetWithDecay =
                                            (decayX > targetX && targetX == screenWidth) ||
                                                    (decayX < targetX && targetX == 0f)

                                        if (reachTargetWithDecay) {
                                            translationX.animateDecay(
                                                initialVelocity = velocity,
                                                animationSpec = decay
                                            )
                                        } else {
                                            translationX.animateTo(
                                                targetValue = targetX,
                                                initialVelocity = velocity
                                            )
                                        }

                                    }
                                )
                                .fillMaxWidth(),
                            onClick = {
                                visionViewModel.changeMode {
                                    navController.navigate(route = AppScreens.Reader.name + "/${data.id}")
                                }
                            },
                            header = {
                                Header(
                                    if (data.title.length == 15) "${data.title.trim()}..."
                                    else data.title.trim(),
                                    createdAt = data.createdAt
                                )
                            },
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

                }
            }
            item { Spacer(modifier = Modifier.height(4.dp)) }
        }
    }
}

