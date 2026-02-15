package com.example.application.navigation

sealed class Screen(val route: String) {

    object Splash : Screen("splash")

    object Login : Screen("login")
    object Signup : Screen("signup")
    object Main : Screen("main")
    object Home : Screen("home")
    object Search : Screen("search")
    object Chat : Screen("chat")
    object Profile : Screen("profile")
}
