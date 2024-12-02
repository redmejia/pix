package com.binarystack01.pix.presentation.ui.navigation

import com.binarystack01.pix.R

sealed class BottomBarItem (
    val route: String,
    val title: String,
    val selected: Int,
    val unselected: Int,
){
    data object Camera: BottomBarItem(
        route = "Camera",
        title = "Camera",
        selected = R.drawable.camera_filled,
        unselected = R.drawable.camera_outlined
    )

    data object Gallery: BottomBarItem(
        route = "Gallery",
        title = "Gallery",
        selected = R.drawable.image_filled,
        unselected = R.drawable.image_outlined
    )

    data object List: BottomBarItem(
        route = "RecognitionList",
        title = "My List",
        selected = R.drawable.view_list_filled,
        unselected = R.drawable.view_list_outlined
    )

}


