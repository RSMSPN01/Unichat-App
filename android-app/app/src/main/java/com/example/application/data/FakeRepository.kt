package com.example.application.data

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.example.application.model.Message
import com.example.application.model.Post
import com.example.application.model.User

class FakeRepository : Repository {

    // ---------------- USERS ----------------

    private val users = listOf(
        User(id = "user_0", username = "User 0"),
        User(id = "user_1", username = "User 1"),
        User(id = "user_2", username = "User 2"),
        User(id = "user_3", username = "User 3"),
        User(id = "user_4", username = "User 4")
    )

    override fun getUsers(): List<User> = users

    // ---------------- POSTS ----------------

    private val posts = mutableStateListOf<Post>().apply {
        repeat(20) { index ->
            add(
                Post(
                    id = index,
                    ownerId = "user_${index % 5}", // distribute posts among users
                    content = "This is post number $index"
                )
            )
        }
    }

    override fun getPosts(): SnapshotStateList<Post> = posts

    override fun addPost(post: Post) {
        posts.add(0, post)
    }

    override fun updatePost(post: Post) {
        val index = posts.indexOfFirst { it.id == post.id }
        if (index != -1) {
            posts[index] = post
        }
    }

    // ---------------- CHAT ----------------

    private val chatMessages =
        mutableMapOf<String, SnapshotStateList<Message>>()

    override fun getMessages(userId: String): SnapshotStateList<Message> {
        return chatMessages.getOrPut(userId) {
            mutableStateListOf(
                Message("Hey bro", false, System.currentTimeMillis()),
                Message("Hello!", true, System.currentTimeMillis())
            )
        }
    }

    override fun sendMessage(userId: String, message: Message) {
        val messages = chatMessages.getOrPut(userId) {
            mutableStateListOf()
        }
        messages.add(message)
    }

    override fun getLastMessage(userId: String): Message? {
        return chatMessages[userId]?.lastOrNull()
    }
}
