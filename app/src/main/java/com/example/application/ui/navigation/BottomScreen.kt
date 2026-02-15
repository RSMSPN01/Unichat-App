package com.example.application.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomScreen(
    val route: String,
    val title: String,
    val icon: ImageVector
) {

    object Home : BottomScreen(
        route = "home",
        title = "Home",
        icon = Icons.Default.Home
    )

    object Search : BottomScreen(
        route = "search",
        title = "Search",
        icon = Icons.Default.Search
    )

    object Chat : BottomScreen(
        route = "chat",
        title = "Chat",
        icon = Icons.Default.Chat
    )

    object Profile : BottomScreen(
        route = "profile/{username}",
        title = "Profile",
        icon = Icons.Default.Person
    ) {
        fun createRoute(username: String) = "profile/$username"
    }
}
