package com.binarystack01.pix.presentation.ui.navigation

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.binarystack01.pix.ui.theme.BlackPrimary0
import com.binarystack01.pix.ui.theme.BluePrimary50
import com.binarystack01.pix.ui.theme.BlueSecondary60

@Composable
fun BottomBar(
    navController: NavHostController,
) {

    val items = listOf(
        BottomBarItem.Camera,
        BottomBarItem.Gallery,
        BottomBarItem.List // RecognitionList Screen
    )

    NavigationBar(
        containerColor = BlackPrimary0,
        contentColor = BlackPrimary0
    ) {

        val selectedColor = BluePrimary50
        val unselectedColor = BlueSecondary60

        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        items.forEach { screen ->

            NavigationBarItem(
                selected = currentRoute == screen.route,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        modifier = Modifier.size(40.dp),
                        painter = painterResource(
                            id = if (currentRoute == screen.route)
                                screen.selected else screen.unselected
                        ),
                        tint = if (currentRoute == screen.route)
                            selectedColor else unselectedColor,
                        contentDescription = screen.title
                    )
                },
                label = {
                    Text(
                        text = screen.title,
                        fontWeight = FontWeight.SemiBold,
                        color = if (currentRoute == screen.route)
                            selectedColor else unselectedColor,
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = BlackPrimary0
                )
            )
        }
    }
}
