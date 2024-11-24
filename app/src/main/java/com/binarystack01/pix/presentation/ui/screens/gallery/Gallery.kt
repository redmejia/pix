package com.binarystack01.pix.presentation.ui.screens.gallery

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.binarystack01.pix.presentation.viewmodel.captureviewmodel.CaptureViewModel
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
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(3),
                        contentPadding = PaddingValues(2.dp),
                        verticalArrangement = Arrangement.spacedBy(2.dp),
                        horizontalArrangement = Arrangement.spacedBy(2.dp),
                        content = {
                            items(photoState.photos) { photo ->
                                AsyncImage(
//                                    modifier = Modifier.clickable {
//                                        selectPhotoId.value = photo.id
//                                    },
                                    model = File(photo.path),
                                    contentDescription = null
                                )
                            }
                        },
                    )
//                    photoState.photo?.let { photo ->
//                        PhotoViewer(photo = photo, open = openViewer)
//                    }
                }
            }
        }
    }
}
