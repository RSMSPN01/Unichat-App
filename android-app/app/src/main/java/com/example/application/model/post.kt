package com.example.application.model

data class Post(
    val id: Int,
    val ownerId: String,
    val content: String,
    val likedBy: Set<String> = emptySet(),
    val comments: List<Comment> = emptyList()
) {
    val likeCount: Int
        get() = likedBy.size
}
