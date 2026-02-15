package com.example.application.data

import androidx.compose.runtime.snapshots.SnapshotStateList
import com.example.application.model.Message
import com.example.application.model.Post
import com.example.application.model.User

interface Repository {

    // ---------------- POSTS ----------------
    fun getPosts(): SnapshotStateList<Post>
    fun addPost(post: Post)
    fun updatePost(post: Post)

    // ---------------- USERS ----------------
    fun getUsers(): SnapshotStateList<User>

    // ---------------- CHAT ----------------
    fun getMessages(userId: String): SnapshotStateList<Message>
    fun sendMessage(userId: String, message: Message)
    fun getLastMessage(userId: String): Message?
}
