package com.example.application.ui.navigation

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController


@Composable
fun BottomNavigationBar(navController: NavHostController) {

    val items = listOf(
        BottomScreen.Home,
        BottomScreen.Search,
        BottomScreen.Chat,
        BottomScreen.Profile
    )

    NavigationBar {

        val currentDestination =
            navController.currentBackStackEntryAsState().value?.destination

        items.forEach { screen ->

            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = screen.icon,
                        contentDescription = screen.title
                    )
                },
                label = { Text(screen.title) },
                selected = currentDestination?.route == screen.route,
                onClick = {

                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.startDestinationId)
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}
