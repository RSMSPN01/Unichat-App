package com.example.application.model

data class Post(
    val id: Int,
    val ownerId: String,
    val content: String? = null,          // Optional text
    val imageUrl: String? = null,         // Optional image
    val likedBy: Set<String> = emptySet(),
    val comments: List<Comment> = emptyList()
) {
    val likeCount: Int
        get() = likedBy.size
}
