package com.example.application.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.application.ui.auth.login.LoginScreen
import com.example.application.ui.auth.signup.SignupScreen
import com.example.application.ui.auth.splash.SplashScreen
import com.example.application.ui.main.MainScreen

@Composable
fun AppNavGraph(navController: NavHostController) {

    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {

        composable(Screen.Splash.route) {
            SplashScreen(navController)
        }

        composable(Screen.Login.route) {
            LoginScreen(navController)
        }

        composable(Screen.Signup.route) {
            SignupScreen(navController)
        }

        // 🔥 THIS is where MainScreen should be
        composable(Screen.Main.route) {
            MainScreen()
        }
    }
}
