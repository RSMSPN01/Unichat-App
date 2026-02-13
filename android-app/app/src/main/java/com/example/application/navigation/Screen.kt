package com.example.application.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Home : Screen("home")

    object Login : Screen("login")
    object Signup : Screen("signup")
}
