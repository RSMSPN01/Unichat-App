package com.example.application.model

data class User(
    val id: String,
    val username: String,
    val imageUrl: String,
    val followers: MutableSet<String> = mutableSetOf(),
    val following: MutableSet<String> = mutableSetOf()
)
