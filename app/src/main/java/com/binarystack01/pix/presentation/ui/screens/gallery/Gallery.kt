package com.binarystack01.pix.presentation.ui.screens.gallery

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import com.binarystack01.pix.presentation.ui.screens.gallery.photoviewer.PhotoViewer
import com.binarystack01.pix.presentation.viewmodel.captureviewmodel.CaptureViewModel


@Composable
fun Gallery(
    captureViewModel: CaptureViewModel,
) {
    val photoState by captureViewModel.photoState.collectAsState()
    val selectPhotoId = remember { mutableStateOf("") }
    val openViewer = remember { mutableStateOf(false) }

    LaunchedEffect(selectPhotoId.value) {
        if (selectPhotoId.value.isNotBlank()) {
            captureViewModel.loadPhoto(selectPhotoId.value)
            selectPhotoId.value = ""
            openViewer.value = !openViewer.value
        }
    }

    Surface {
        Box(modifier = Modifier.fillMaxSize()) {
            if (photoState.photos.isEmpty()) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text = "No Photo.")
                }
            } else {
                Box(modifier = Modifier.fillMaxSize()) {
                    LazyVerticalStaggeredGrid(
                        columns = StaggeredGridCells.Fixed(3),
                        verticalItemSpacing = 4.dp,
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        content = {
                            items(photoState.photos) { photo ->
                                Image(
                                    modifier = Modifier.clickable {
                                        selectPhotoId.value = photo.id
                                    },
                                    bitmap = photo.photo.asImageBitmap(), contentDescription = null
                                )
                            }
                        },
                        modifier = Modifier.fillMaxSize()
                    )
                    photoState.photo?.let { photo ->
                        PhotoViewer(photo = photo, open = openViewer)
                    }
                }
            }
        }
    }
}
