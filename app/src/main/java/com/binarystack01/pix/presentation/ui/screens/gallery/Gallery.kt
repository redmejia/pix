package com.binarystack01.pix.presentation.ui.screens.gallery

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.binarystack01.pix.presentation.ui.components.actionbuttons.ActionButton
import com.binarystack01.pix.presentation.ui.screens.gallery.photoviewer.PhotoViewer
import com.binarystack01.pix.presentation.viewmodel.captureviewmodel.CaptureViewModel
import com.binarystack01.pix.ui.theme.BlackPrimary95
import com.binarystack01.pix.ui.theme.BlueSecondary60
import com.binarystack01.pix.ui.theme.WhitePrimary0
import java.io.File

@Composable
fun Gallery(
    captureViewModel: CaptureViewModel,
) {
    val photoState by captureViewModel.photoState.collectAsState()
    val selectPhotoId = remember { mutableStateOf("") }
    val openViewer = remember { mutableStateOf(false) }

    LaunchedEffect(selectPhotoId.value) {
        if (selectPhotoId.value.isNotBlank()) {
            captureViewModel.loadPhoto(fileName = selectPhotoId.value)
            selectPhotoId.value = ""
            openViewer.value = !openViewer.value
        }
    }

    Surface {
        Box(
            modifier = Modifier
                .fillMaxSize(),
        ) {
            if (photoState.photos.isEmpty()) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    // TODO: Add an icon or text when gallery list is empty 
                }
            } else {
                Box(modifier = Modifier.fillMaxSize()) {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(3),
                        contentPadding = PaddingValues(2.dp),
                        verticalArrangement = Arrangement.spacedBy(2.dp),
                        horizontalArrangement = Arrangement.spacedBy(2.dp),
                        content = {
                            items(photoState.photos) { photo ->
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.BottomCenter
                                ) {
                                    AsyncImage(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .clickable {
                                                selectPhotoId.value = photo.fileName
                                            },
                                        model = File(photo.thumbnailPath),
                                        contentDescription = null
                                    )

                                    Box(
                                        modifier = Modifier
                                            .background(color = BlackPrimary95)
                                            .padding(horizontal = 3.dp)
                                            .fillMaxSize(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = photo.createdAt,
                                                fontSize = 8.sp,
                                                color = BlueSecondary60,
                                                fontWeight = FontWeight.ExtraBold
                                            )
                                            ActionButton(
                                                colors = IconButtonDefaults.iconButtonColors(
                                                    contentColor = WhitePrimary0
                                                ),
                                                onClick = {
                                                    captureViewModel.deleteImage(photo = photo)
                                                },
                                                imageIconVector = Icons.Default.Delete
                                            )
                                        }
                                    }
                                }
                            }
                        },
                    )

                    photoState.photo?.let { photo ->
                        PhotoViewer(
                            captureViewModel = captureViewModel,
                            photo = photo,
                            open = openViewer
                        )
                    }
                }
            }
        }
    }
}
