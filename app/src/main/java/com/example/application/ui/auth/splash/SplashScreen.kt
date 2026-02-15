package com.example.application.ui.auth.splash

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.application.navigation.Screen
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavHostController) {

    val scale = remember { Animatable(1.1f) }
    val alpha = remember { Animatable(0f) }

    LaunchedEffect(Unit) {

        alpha.animateTo(
            targetValue = 1f,
            animationSpec = tween(800)
        )

        scale.animateTo(
            targetValue = 1f,
            animationSpec = tween(800)
        )

        delay(700)

        navController.navigate(Screen.Login.route) {
            popUpTo(Screen.Splash.route) { inclusive = true }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        Text(
            text = "Unichat",
            fontSize = 48.sp,
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier
                .alpha(alpha.value)
                .scale(scale.value)
        )
    }
}
