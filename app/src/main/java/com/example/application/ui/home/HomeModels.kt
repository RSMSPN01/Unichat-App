package com.example.application.ui.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

data class Story(
    val id: Int,
    val userId: String
)

data class Post(
    val id: Int,
    val username: String,
    val content: String
)
