package com.example.application.ui.main

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.application.ui.home.HomeScreen
import com.example.application.ui.navigation.BottomNavigationBar
import com.example.application.ui.navigation.BottomScreen


@Composable
fun MainScreen() {

    val navController = rememberNavController()

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
                SearchScreen()
            }

            composable(BottomScreen.Chat.route) {
                ChatScreen()
            }

            composable(BottomScreen.Profile.route) {
                ProfileScreen()
            }
        }
    }
}

@Composable
fun ProfileScreen() {
    TODO("Not yet implemented")
}

@Composable
fun ChatScreen() {
    TODO("Not yet implemented")
}

@Composable
fun SearchScreen() {
    TODO("Not yet implemented")
}
