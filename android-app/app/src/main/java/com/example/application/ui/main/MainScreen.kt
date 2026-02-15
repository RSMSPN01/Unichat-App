package com.example.application.ui.main

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.application.ui.chat.ChatDetailScreen
import com.example.application.ui.home.HomeScreen
import com.example.application.ui.navigation.BottomNavigationBar
import com.example.application.ui.navigation.BottomScreen
import com.example.application.ui.profile.ProfileScreen
import com.example.application.ui.search.SearchScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.application.ui.chat.ChatListScreen
import com.example.application.ui.viewmodel.AppViewModel
@Composable
fun MainScreen() {

    val navController = rememberNavController()

    val viewModel: AppViewModel = viewModel()

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController)
        }
    ) { innerPadding ->

        NavHost(
            navController = navController,
            startDestination = BottomScreen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(BottomScreen.Home.route) {
                HomeScreen()
            }

            composable(BottomScreen.Search.route) {
                SearchScreen(navController, viewModel)
            }

            composable(BottomScreen.Chat.route) {
                ChatListScreen(navController, viewModel)
            }

            composable("chat/{username}") { backStackEntry ->
                val username =
                    backStackEntry.arguments?.getString("username") ?: ""
                ChatDetailScreen(username, viewModel)
            }

            composable("profile/{username}") { backStackEntry ->
                val username =
                    backStackEntry.arguments?.getString("username") ?: ""
                ProfileScreen(username, viewModel)
            }
        }
    }
}
